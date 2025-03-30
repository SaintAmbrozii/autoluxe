package com.example.autoluxe.service;


import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.dto.AdminDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.exception.NotFoundException;
import com.example.autoluxe.payload.*;
import com.example.autoluxe.payload.addsubuser.AddSubUserRequest;
import com.example.autoluxe.payload.addsubuser.AddSubUserResponse;
import com.example.autoluxe.payload.confirmbuy.ConfirmBuyRequest;
import com.example.autoluxe.payload.confirmbuy.ConfirmByResponse;
import com.example.autoluxe.payload.getbuytoken.GetByTokenRequest;
import com.example.autoluxe.payload.getbuytoken.GetByTokenResponse;
import com.example.autoluxe.payload.getuseraccounts.GetUserAccountResponse;
import com.example.autoluxe.payload.getuseraccounts.GetUserAccounts;
import com.example.autoluxe.payload.getuseraccounts.UserAccountDto;
import com.example.autoluxe.payload.getusertoken.GetUserTokenRequest;
import com.example.autoluxe.payload.getusertoken.GetUserTokenResponse;
import com.example.autoluxe.payload.hideuseracc.HideAccRequest;
import com.example.autoluxe.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String EPIC_URI = "https://epcinfo.ru/api/v2/";

    private static final String partner_token = "1e3972f9908c713356e9eb255947f148";

    private static final Map<String,String> buyTokenMap = new HashMap<>();

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final AccountService accountService;

    public UserService(UserRepo userRepo, PasswordEncoder encoder, AccountService accountService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.accountService = accountService;
    }




    public User save(User user) {
        return userRepo.save(user);
    }


    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public List<UserDto> getAllUsers () {
        return userRepo.findAll().stream().map(UserDto::toDto).collect(Collectors.toList());
    }


    public Optional<UserDto> findByEmail (String email) {
        return userRepo.findUserByEmail(email).stream().map(UserDto::toDto).findFirst();
    }

    public UserDto getUserToken (Long userId,User user) {

        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RestClient client = RestClient.create();

        GetUserTokenRequest tokenRequest = GetUserTokenRequest.builder()
                .user_id(inDB.getId())
                .partner_token(user.getPartner_token()).build();

        GetUserTokenResponse response = client.
                post().
                uri(EPIC_URI + "get_user_token").
                body(tokenRequest).
                retrieve()
                .body(GetUserTokenResponse.class);

        inDB.setEpic_token(response.getToken());

        User updated = userRepo.save(inDB);
        return UserDto.toDto(updated);

    }

    public UserDto addSubUser(Long userId) {

        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RestClient client = RestClient.create();

        AddSubUserRequest request = AddSubUserRequest.builder()
                .token(inDB.getEpic_token()).build();

        AddSubUserResponse response = client.
                post().
                uri(EPIC_URI + "add_sub_user").
                body(request).
                retrieve()
                .body(AddSubUserResponse.class);

        List<String> epicId = Collections.singletonList(String.valueOf(response.getEpc_id()));
        inDB.setEpics_ids(epicId);

        User update = userRepo.save(inDB);

        return UserDto.toDto(update);

    }

    public ResponseEntity<ApiResponse> hideAccount(Long userId, Long accountId) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        String epcId = String.valueOf(account.getEpcId());

        HideAccRequest request = HideAccRequest.builder()
                .token(inDB.getEpic_token())
                .epc_ids(epcId).build();

        client.
                post().
                uri(EPIC_URI + "hide_acc").
                body(request).
                retrieve()
                .toBodilessEntity();

        account.setHide(true);
        accountService.save(account);

        return ResponseEntity.ok(new ApiResponse(true,"Hide account sucessfully"));
    }

    public ResponseEntity<ApiResponse> changeUserName(Long userId,Long accountId, ChangeUserNameRequest request) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        String epcId = String.valueOf(account.getEpcId());

        request = ChangeUserNameRequest.builder()
                .epc_id(epcId)
                .name(request.getName())
                .token(inDB.getEpic_token()).build();

        client.
                post().
                uri(EPIC_URI + "change_user_name").
                body(request).
                retrieve()
                .toBodilessEntity();

        account.setName(request.getName());
        accountService.save(account);

        return ResponseEntity.ok(new ApiResponse(true,"ChangeUserName sucessfully"));
    }

    public ResponseEntity<ApiResponse> changeUserLogin(Long userId,Long accountId, ChangeUserLoginRequest request) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        String epcId = String.valueOf(account.getEpcId());

        request = ChangeUserLoginRequest.builder()
                .epc_id(epcId)
                .login(request.getLogin())
                .token(inDB.getEpic_token()).build();

        client.
                post().
                uri(EPIC_URI + "change_user_login").
                body(request).
                retrieve()
                .toBodilessEntity();

        account.setADName(request.getLogin());
        accountService.save(account);

        return ResponseEntity.ok(new ApiResponse(true,"ChangeUserLogin sucessfully"));
    }

    public ResponseEntity<ApiResponse> changeUserPass(Long userId,Long accountId, ChangeUserPass request) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        String epcId = String.valueOf(account.getEpcId());

        request = ChangeUserPass.builder()
                .epc_id(epcId)
                .pass(request.getPass())
                .token(inDB.getEpic_token()).build();

        client.
                post().
                uri(EPIC_URI + "change_user_pass").
                body(request).
                retrieve()
                .toBodilessEntity();

        account.setPass(request.getPass());
        accountService.save(account);

        return ResponseEntity.ok(new ApiResponse(true,"ChangeUserPass sucessfully"));
    }

    public ResponseEntity<GetByTokenResponse> getByToken (Long userId,Long accountId, GetByTokenRequest request) {

        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        Integer epcId = account.getEpcId();

        request = GetByTokenRequest.builder()
                .token(inDB.getEpic_token())
                .products(request.getProducts())
                .user_ids(String.valueOf(epcId))
                .days(request.getDays()).build();

        GetByTokenResponse response = client.
                post().
                uri(EPIC_URI + "get_buy_token").
                body(request).
                retrieve()
                .body(GetByTokenResponse.class);

        buyTokenMap.put(inDB.getEpic_token(),response.getToken());

        return ResponseEntity.ok(response);

    }


    public ResponseEntity<ConfirmByResponse> confirmBuy (Long userId) {

        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RestClient client = RestClient.create();

        String Btoken = buyTokenMap.get(inDB.getEpic_token());

        ConfirmBuyRequest request = ConfirmBuyRequest.
                builder()
                .token(inDB.getEpic_token())
                .Btoken(Btoken).build();

        ConfirmByResponse response = client.
                post().
                uri(EPIC_URI + "confirm_buy").
                body(request).
                retrieve()
                .body(ConfirmByResponse.class);

        List<UserAccount> accounts = response.getByAccountDtoList().stream().map(a-> {
            UserAccount account = new UserAccount();
            account.setEpcId(a.getEpc_id());
            account.setLogin(a.getLogin());
            account.setPass(a.getPass());
            return account;
        }).collect(Collectors.toList());

        accountService.accountSaveList(accounts);


        return ResponseEntity.ok(response);
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

        List<UserAccount> accounts = response.getAccounts().stream().map(a-> {
            UserAccount account = new UserAccount();
            account.setADName(a.getAD_name());
            account.setHide(a.isHide());
            account.setRFCExpires(a.getRFC_expires());
            account.setStatus(a.getStatus());
            account.setPass(a.getPass());
            return account;
        }).collect(Collectors.toList());

        accountService.accountSaveList(accounts);

        List<Integer> epcIdList = Arrays.stream(response.getAccounts()
                .toArray(new UserAccountDto[0])).map(UserAccountDto::getId).collect(Collectors.toList());

        List<String> ids = epcIdList.stream().map(Object::toString).collect(Collectors.toList());

        inDB.setEpics_ids(ids);

        userRepo.save(inDB);

        return ResponseEntity.ok(response);

    }





    public AdminDto addPartnerToken(User user, AddPartnerToken token) {
        User inDb = userRepo.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        inDb.setPartner_token(token.getPartner_token());

        User update = userRepo.save(inDb);

        return AdminDto.toDto(update);
    }



    public UserDto findById(Long id) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserDto.toDto(inDB);
    }

    public AdminDto getProfile(Long id) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return AdminDto.toDto(inDB);
    }

    public UserDto updateUser (Long id, User user) {
        User inDB = userRepo.
                findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        inDB.setEmail(user.getEmail());
        inDB.setName(user.getName());
        inDB.setPassword(encoder.encode(user.getPassword()));
        return UserDto.toDto(inDB);
    }



    public boolean doesUsernameExists(String username){
        return userRepo.findUserByEmail(username).isPresent();
    }




}
