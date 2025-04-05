package com.example.autoluxe.service;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.exception.ApiMethodException;
import com.example.autoluxe.exception.ApiTokenNotFoundException;
import com.example.autoluxe.exception.NotFoundException;
import com.example.autoluxe.payload.confirmbuy.ConfirmBuyRequest;
import com.example.autoluxe.payload.confirmbuy.ConfirmByAccountDto;
import com.example.autoluxe.payload.confirmbuy.ConfirmByResponse;
import com.example.autoluxe.payload.getuseraccounts.GetUserAccountResponse;
import com.example.autoluxe.payload.getuseraccounts.GetUserAccounts;
import com.example.autoluxe.payload.getusertoken.GetUserTokenRequest;
import com.example.autoluxe.payload.getusertoken.GetUserTokenResponse;
import com.example.autoluxe.repo.UserRepo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jaxb.runtime.api.TypeReference;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ApiService {


    private final UserRepo userRepo;
    private final AccountService accountService;


    public ApiService(UserRepo userRepo, AccountService accountService) {
        this.userRepo = userRepo;
        this.accountService = accountService;
    }

    private static final String EPIC_URI = "https://epcinfo.ru/api/v2/";

    private static final String partner_token = "1e3972f9908c713356e9eb255947f148";

    public void getUserToken (Long userId) {

        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RestClient client = RestClient.create();

        GetUserTokenRequest tokenRequest = GetUserTokenRequest.builder()
                .user_id(inDB.getId())
                .partner_token(partner_token).build();

        GetUserTokenResponse response = client.
                post().
                uri(EPIC_URI + "get_user_token").
                body(tokenRequest).
                retrieve()
                .body(GetUserTokenResponse.class);

        inDB.setEpic_token(response.getToken());

        userRepo.save(inDB);

    }


    public ResponseEntity<GetUserAccountResponse> getUserAccount(Long userId){

        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RestClient client = RestClient.create();

        GetUserAccounts request = GetUserAccounts.
                builder()
                .token(inDB.getEpic_token()).build();

        GetUserAccountResponse response = client.
                post().
                uri(EPIC_URI + "get_user_accounts").
                body(request).
                retrieve()
                .body(GetUserAccountResponse.class);
        List<UserAccount> current = accountService.findAllByUserId(inDB.getId());

        if (current.isEmpty()) {
            List<UserAccount> accounts = response.getAccounts().stream().map(a-> {
                UserAccount account = new UserAccount();
                account.setEpcId(a.getId());
                account.setLogin(a.getName());
                account.setADName(a.getAD_name());
                account.setHide(a.isHide());
                account.setRFCExpires(a.getRFC_expires());
                account.setStatus(a.getStatus());
                account.setPass(a.getPass());
                account.setUserId(inDB.getId());
                return account;
            }).collect(Collectors.toList());

            accountService.accountSaveList(accounts);
        }

        return ResponseEntity.ok(response);

    }

    public void confirmBuy (String Btoken, String usertoken) {


        RestClient client = RestClient.create();

        ConfirmBuyRequest request = ConfirmBuyRequest.
                builder()
                .token(usertoken)
                .Btoken(Btoken).build();

        ParameterizedTypeReference<List<ConfirmByAccountDto>> confirmList = new ParameterizedTypeReference<List<ConfirmByAccountDto>>() {
            @Override
            public boolean equals(Object other) {
                return super.equals(other);
            }
        };

        ObjectMapper mapper = new ObjectMapper();


        String response = client.
                post().
                uri(EPIC_URI + "confirm_buy").
                body(request)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(String.class);


    //    List<UserAccount> accounts = response.getByAccountDtoList().stream().map(a-> {
    //        UserAccount account = new UserAccount();
   ///         account.setEpcId(a.getEpc_id());
    //        account.setLogin(a.getLogin());
    //        account.setPass(a.getPass());
    //        account.setRFCExpires(a.getExpires());

    //        return account;
     //   }).collect(Collectors.toList());



    }

    public void confirm (String Btoken, String usertoken) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES,true);

        RestClient client = RestClient.create();

        ConfirmBuyRequest request = ConfirmBuyRequest.
                builder()
                .token(usertoken)
                .Btoken(Btoken).build();

        List<ConfirmByAccountDto> response = client.
                post().
                uri(EPIC_URI + "confirm_buy").
                exchange((clientRequest, clientResponse) -> {
                    if (clientResponse.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                        throw new ApiTokenNotFoundException("сервер не отвечает");
                    }
                    else if (clientResponse.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(200))) {
                        return mapper.readValue(clientResponse.getBody(), ConfirmByResponse.class);
                    } else {
                        throw new ApiMethodException("");
                    }
                }).getByAccountDtoList();


      //  accountService.accountSaveList(accounts);

    }
}
