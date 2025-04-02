package com.example.autoluxe.service;


import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.dto.AdminDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.events.BuyEpcTokenEvent;
import com.example.autoluxe.events.BuyEpcTokenEventListener;
import com.example.autoluxe.events.GetUserAccountsEvent;
import com.example.autoluxe.events.GetUserAccountsListener;
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
import com.example.autoluxe.payload.getusertoken.GetUserTokenRequest;
import com.example.autoluxe.payload.getusertoken.GetUserTokenResponse;
import com.example.autoluxe.payload.hideuseracc.HideAccRequest;
import com.example.autoluxe.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String EPIC_URI = "https://epcinfo.ru/api/v2/";

    private static final String partner_token = "1e3972f9908c713356e9eb255947f148";


    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final AccountService accountService;
    private final GetUserAccountsListener getUserAccountsListener;
    private final PaymentService paymentService;
    private final BuyEpcTokenEventListener buyEpcTokenEventListener;

    public UserService(UserRepo userRepo, PasswordEncoder encoder,
                       AccountService accountService,
                       GetUserAccountsListener getUserAccountsListener,
                       PaymentService paymentService, BuyEpcTokenEventListener buyEpcTokenEventListener) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.accountService = accountService;
        this.getUserAccountsListener = getUserAccountsListener;
        this.paymentService = paymentService;
        this.buyEpcTokenEventListener = buyEpcTokenEventListener;
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

    public void addSubUser(Long userId) {

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

        getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));

    }

    public void hideAccount(Long userId, Long accountId) {
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

        getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));
    }

    public void changeUserName(Long userId,Long accountId, ChangeUserNameRequest request) {
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

        getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));

    }

    public void changeUserLogin(Long userId,Long accountId, ChangeUserLoginRequest request) {
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

        getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));


    }

    public void changeUserPass(Long userId,Long accountId, ChangeUserPass request) {
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

        getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));


    }

    public void getByToken (Long userId,Long accountId, GetByTokenRequest request) {

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

        buyEpcTokenEventListener.onApplicationEvent(new BuyEpcTokenEvent(response.getToken(),inDB.getEpic_token()));


    }



    public void getUserAccount(Long userId){

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


    }




    public UserDto findById(Long id) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserDto.toDto(inDB);
    }

    @Transactional
    public UserDto addBalance(Long id, Double balance) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        inDB.setBalance(balance);
        Payments payments = new Payments();
        payments.setCreated(LocalDateTime.now());
        payments.setManagerId(32L);
        payments.setUserId(inDB.getId());
        payments.setSumma(balance);
        payments.setPayAdmin(true);
        paymentService.save(payments);

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
