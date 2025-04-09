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
import com.example.autoluxe.repo.AccountRepo;
import com.example.autoluxe.repo.UserRepo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiService {


    private final UserRepo userRepo;
    private final AccountRepo accountRepo;
    private final AccountService accountService;


    public ApiService(UserRepo userRepo, AccountRepo accountRepo, AccountService accountService) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
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
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON)
                .body(request).
                retrieve()
                .body(GetUserAccountResponse.class);

        List<UserAccount> accounts = accountRepo.findAllByUserId(inDB.getId());

        if (accounts.isEmpty()) {
            List<UserAccount> current = response.getAccounts().stream().map(a-> {
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
            accountService.accountSaveList(current);
        }


        return ResponseEntity.ok(response);

    }

    public void confirmBuy (String Btoken, String usertoken) {

        RestClient client = RestClient.create();

        ConfirmBuyRequest request = ConfirmBuyRequest.
                builder()
                .token(usertoken)
                .btoken(Btoken).build();

        System.out.println(request);

        ConfirmByResponse response = client.
                post().
                uri(EPIC_URI + "confirm_buy").
                body(request)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(ConfirmByResponse.class);

        List<UserAccount> toEntity = response.getAccounts().stream().map(a-> {
            UserAccount account = accountRepo.
                    findUserAccountByEpcId(a.getEpc_id()).orElseThrow();
            account.setLogin(a.getLogin());
            account.setPass(a.getPass());
            account.setRFCExpires(a.getExpires());
           return account;
        }).collect(Collectors.toList());

        accountService.accountSaveList(toEntity);

    }


}
