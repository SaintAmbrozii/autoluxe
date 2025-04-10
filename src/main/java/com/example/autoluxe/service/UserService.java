package com.example.autoluxe.service;


import com.example.autoluxe.domain.Calculate;
import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.events.BuyEpcTokenEvent;
import com.example.autoluxe.events.BuyEpcTokenEventListener;
import com.example.autoluxe.events.GetUserAccountsListener;
import com.example.autoluxe.exception.NotFoundException;
import com.example.autoluxe.payload.addbalance.AddBalance;
import com.example.autoluxe.payload.addsubuser.AddSubUserRequest;
import com.example.autoluxe.payload.addsubuser.AddSubUserResponse;
import com.example.autoluxe.payload.changelogin.ChangeUserLoginRequest;
import com.example.autoluxe.payload.changelogin.UserLoginRequest;
import com.example.autoluxe.payload.changename.ChangeUserNameRequest;
import com.example.autoluxe.payload.changename.UserNameRequest;
import com.example.autoluxe.payload.changepass.ChangeUserPass;
import com.example.autoluxe.payload.getbuytoken.BuyTokenRequest;
import com.example.autoluxe.payload.getbuytoken.GetByTokenRequest;
import com.example.autoluxe.payload.getbuytoken.GetByTokenResponse;
import com.example.autoluxe.payload.getusertoken.GetUserTokenRequest;
import com.example.autoluxe.payload.getusertoken.GetUserTokenResponse;
import com.example.autoluxe.payload.hideuseracc.HideAccRequest;
import com.example.autoluxe.repo.AccountRepo;
import com.example.autoluxe.repo.UserRepo;
import com.example.autoluxe.utils.DateUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    private final AccountRepo accountRepo;
    private final CalculationService calculationService;

    public UserService(UserRepo userRepo, PasswordEncoder encoder,
                       AccountService accountService,
                       GetUserAccountsListener getUserAccountsListener,
                       PaymentService paymentService, BuyEpcTokenEventListener buyEpcTokenEventListener,
                       AccountRepo accountRepo, CalculationService calculationService) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.accountService = accountService;
        this.getUserAccountsListener = getUserAccountsListener;
        this.paymentService = paymentService;
        this.buyEpcTokenEventListener = buyEpcTokenEventListener;
        this.accountRepo = accountRepo;
        this.calculationService = calculationService;
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

        UserAccount account = new UserAccount();
        account.setEpcId(response.getEpc_id());
        accountRepo.save(account);

      //  getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));

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

        account.setHide(true);
        accountRepo.save(account);

    //    getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));
    }

    public UserAccount changeUserName(Long userId, Long accountId, UserNameRequest userNameRequest) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        String epcId = String.valueOf(account.getEpcId());

       ChangeUserNameRequest request = ChangeUserNameRequest.builder()
                .epc_id(epcId)
                .name(userNameRequest.getName())
                .token(inDB.getEpic_token()).build();

        client.
                post().
                uri(EPIC_URI + "change_user_name").
                body(request).
                retrieve()
                .toBodilessEntity();

        account.setName(userNameRequest.getName());

        return accountRepo.save(account);

    //    getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));

    }

    public UserAccount changeUserLogin(Long userId,Long accountId, UserLoginRequest loginRequest) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        String epcId = String.valueOf(account.getEpcId());

        ChangeUserLoginRequest request = ChangeUserLoginRequest.builder()
                .epc_id(epcId)
                .login(loginRequest.getLogin())
                .token(inDB.getEpic_token()).build();

        client.
                post().
                uri(EPIC_URI + "change_user_login").
                body(request).
                retrieve()
                .toBodilessEntity();

        account.setLogin(loginRequest.getLogin());
       return accountRepo.save(account);
    //    getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));

    }

    public UserAccount changeUserPass(Long userId,Long accountId, ChangeUserPass userPass) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        String epcId = String.valueOf(account.getEpcId());

        ChangeUserPass request = ChangeUserPass.builder()
                .epc_id(epcId)
                .pass(userPass.getPass())
                .token(inDB.getEpic_token()).build();

        client.
                post().
                uri(EPIC_URI + "change_user_pass").
                body(request).
                retrieve()
                .toBodilessEntity();

        account.setPass(userPass.getPass());
      return   accountRepo.save(account);
    //    getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));
    }

    @Transactional
    public void getByToken (Long userId, Long accountId, BuyTokenRequest buyTokenRequest) {

        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        UserAccount account = accountService.findById(accountId)
                .orElseThrow(()->new NotFoundException("AccountNotFound"));

        RestClient client = RestClient.create();

        Integer epcId = account.getEpcId();

    //    Optional<Calculate> calculate = Optional.ofNullable(calculationService.calculate(buyTokenRequest.getParam(), buyTokenRequest.getDays()));

    //    Calculate currentCalc = calculate.get();

        Double price = caclucate(buyTokenRequest.getParam(),buyTokenRequest.getDays());

        if (inDB.getBalance().doubleValue()>= price) {

            BigDecimal buyAccountPrice = inDB.getBalance().subtract(BigDecimal.valueOf(price));


            String params = String.valueOf(buyTokenRequest.getParam()).replace("[", "")
                    .trim().replaceAll(" ","").trim().replaceAll("\\]","").trim();

            System.out.println(params);

            GetByTokenRequest request = GetByTokenRequest.builder()
                    .token(inDB.getEpic_token())
                    .products(params)
                    .user_ids(String.valueOf(epcId))
                    .days(buyTokenRequest.getDays()).build();

            GetByTokenResponse response = client.
                    post().
                    uri(EPIC_URI + "get_buy_token").
                    body(request).
                    retrieve()
                    .body(GetByTokenResponse.class);

            buyEpcTokenEventListener.onApplicationEvent(new BuyEpcTokenEvent(response.getToken(),inDB.getEpic_token()));

            inDB.setBalance(buyAccountPrice);
            userRepo.save(inDB);

            Payments payments = new Payments();
            payments.setUserId(inDB.getId());
            payments.setSumma(BigDecimal.valueOf(price));
            payments.setUserEmail(inDB.getEmail());
            payments.setTimestamp(ZonedDateTime.now());
            paymentService.save(payments);

        }


    }


    public UserDto findById(Long id) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserDto.toDto(inDB);
    }

    @Transactional
    public UserDto addBalance(Long id, User user, AddBalance balance) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        inDB.setBalance(BigDecimal.valueOf(balance.getBalance()));
        Payments payments = new Payments();
        payments.setTimestamp(ZonedDateTime.now());
        payments.setManagerId(user.getId());
        payments.setUserId(inDB.getId());
        payments.setSumma(BigDecimal.valueOf(balance.getBalance()));
        paymentService.save(payments);
        return UserDto.toDto(inDB);
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


    private Double caclucate (List<Integer> params,Integer days) {
        if (params.contains(84) && days == 30) {
            return 5500.00;
        }
        if (params.contains(84) & params.contains(72) && days == 30) {
            return 6500.00;
        }
        return null;
    }

}
