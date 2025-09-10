package com.example.autoluxe.service;


import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.dto.UserAccountDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.dto.UserProfileDto;
import com.example.autoluxe.events.BuyEpcTokenEvent;
import com.example.autoluxe.events.BuyEpcTokenEventListener;
import com.example.autoluxe.events.GetUserAccountsListener;
import com.example.autoluxe.exception.ApiClientException;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (req, resp) -> {
                            var msg = "Failed to create user token response from string: %s";
                            throw new ApiClientException(msg,HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                )
                .body(GetUserTokenResponse.class);

        inDB.setEpic_token(response.getToken());

        userRepo.save(inDB);

    }

    public UserAccountDto addSubUser(Long userId) {

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
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (req, resp) -> {
                            var msg = "Failed to create add sub user response from string: %s";
                            throw new ApiClientException(msg,HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                )
                .body(AddSubUserResponse.class);

        String dateTime = "1970-01-01 07:00:00";
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        UserAccount account = new UserAccount();
        account.setUserId(inDB.getId());
        account.setEpcId(response.getEpc_id());
        account.setRFCExpires(LocalDateTime.parse(dateTime,format));
        account.setHide(false);
        UserAccount newAccount =  accountRepo.save(account);
        return UserAccountDto.toDto(newAccount);

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
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (req, resp) -> {
                            var msg = "Failed to hide account response from string: %s";
                            throw new ApiClientException(msg,HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                )
                .toBodilessEntity();



        account.setHide(true);
        accountRepo.save(account);

    //    getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));
    }

    public UserAccountDto changeUserName(Long userId, Long accountId, UserNameRequest userNameRequest) {
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
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (req, resp) -> {
                            var msg = "Failed to chane user name response from string: %s";
                            throw new ApiClientException(msg,HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                )
                .toBodilessEntity();

        account.setName(userNameRequest.getName());
        accountRepo.save(account);

        return UserAccountDto.toDto(account);

    //    getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));

    }

    public UserAccountDto changeUserLogin(Long userId,Long accountId, UserLoginRequest loginRequest) {
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
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (req, resp) -> {
                            var msg = "Failed to change user login response from string: %s";
                            throw new ApiClientException(msg,HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                )
                .toBodilessEntity();

        account.setLogin(loginRequest.getLogin());
        accountRepo.save(account);
       return UserAccountDto.toDto(account);
    //    getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(inDB.getId()));

    }

    public UserAccountDto changeUserPass(Long userId, Long accountId, ChangeUserPass userPass) {
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
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (req, resp) -> {
                            var msg = "Failed tochange user pass response from string: %s";
                            throw new ApiClientException(msg,HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                )
                .toBodilessEntity();

        account.setPass(userPass.getPass());
        accountRepo.save(account);
      return  UserAccountDto.toDto(account);
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
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            (req, resp) -> {
                                var msg = "Failed to get buy token response from string: %s";
                                throw new ApiClientException(msg,HttpStatus.INTERNAL_SERVER_ERROR);
                            }
                    )
                    .body(GetByTokenResponse.class);

            buyEpcTokenEventListener.onApplicationEvent(new BuyEpcTokenEvent(response.getToken(),inDB.getEpic_token()));

            inDB.setBalance(buyAccountPrice);
            userRepo.save(inDB);

            Payments payments = new Payments();
            payments.setUserId(inDB.getId());
            payments.setManagerId(inDB.getId());
            payments.setSumma(BigDecimal.valueOf(price));
            payments.setUserEmail(inDB.getEmail());
            payments.setTimestamp(ZonedDateTime.now());

            paymentService.save(payments);

        }


    }


    public UserDto findById(Long id) {
        User inDB = userRepo.findById(id).
                orElseThrow(() -> new NotFoundException("User not found"));
        return UserDto.toDto(inDB);
    }

    public UserProfileDto findByProfile(Long id) {
        User inDB = userRepo.findById(id).
                orElseThrow(() -> new NotFoundException("User not found"));
        return UserProfileDto.toDto(inDB);
    }

    @Transactional
    public UserDto addBalance(Long id, User user, AddBalance balance) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if (Objects.equals(inDB.getBalance(), BigDecimal.ZERO)) {
            inDB.setBalance(BigDecimal.valueOf(balance.getBalance()));
            userRepo.save(inDB);
        }
        BigDecimal addBalance = BigDecimal.valueOf(balance.getBalance());
        BigDecimal userBalance = inDB.getBalance();

        BigDecimal sum = BigDecimal.valueOf(userBalance.doubleValue()).add(addBalance);
        inDB.setBalance(sum);
        userRepo.save(inDB);

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
        userRepo.save(inDB);
        return UserDto.toDto(inDB);
    }

    @Transactional
    public UserProfileDto updateProfile (User user, UserProfileDto request) {
        User inDB = userRepo.
                findById(user.getId()).orElseThrow(() -> new NotFoundException("User not found"));
       if (request.getName()!=null){
           inDB.setName(request.getName());
       }
       if (request.getPhone()!=null) {
           inDB.setPhone(request.getPhone());
       }
       if (request.getPassword()!=null){
           inDB.setPassword(encoder.encode(request.getPassword()));
       }
       if (request.getEmail()!=null) {
           inDB.setEmail(request.getEmail());
       }
        User newUser = userRepo.save(inDB);
        return UserProfileDto.toDto(newUser);
    }


    public User createTestUser(User user) {
       return userRepo.save(user);

    }

    public void changeUserPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        userRepo.save(user);
    }



    public boolean doesUsernameExists(String username){
        return userRepo.findUserByEmail(username).isPresent();
    }

    //4 - AutoData
    //72 - TechDoc
    //84 - Полный
    // 129 TIS
    //132 - легковой
    //133 - грузовой
    //134 - TechData

    private Double caclucate (List<Integer> params,Integer days) {
        if (params.contains(84) && days == 30) {
            return 5500.00;
        }
        if (params.contains(84) && days == 90) {
            return 15000.00;
        }
        if (params.contains(84) && days == 180){
            return 28800.00;
        }
        if (params.contains(84) && days == 365) {
            return 54000.00;
        }
        if (params.contains(132) && days == 30) {
            return 4500.00;
        }
        if (params.contains(132) && days == 90) {
            return 12500.00;
        }
        if (params.contains(132) && days == 180) {
            return 23500.00;
        }
        if (params.contains(132) && days == 365) {
            return 45000.00;
        }
        if (params.contains(133) && days == 30) {
            return 4500.00;
        }
        if (params.contains(133) && days == 90) {
            return 12500.00;
        }
        if (params.contains(133) && days == 180) {
            return 23500.00;
        }
        if (params.contains(133) && days == 365) {
            return 45000.00;
        }
        if (params.contains(129) && days == 30) {
            return 3000.00;
        }
        if (params.contains(129) && days == 90) {
            return 9000.00;
        }
        if (params.contains(129) && days == 180) {
            return 18000.00;
        }
        if (params.contains(129) && days == 365) {
            return 36000.00;
        }
        if (params.contains(72) && days == 30) {
            return 1000.00;
        }
        if (params.contains(72) && days == 90) {
            return 3000.00;
        }
        if (params.contains(72) && days == 180) {
            return 6000.00;
        }
        if (params.contains(72) && days == 365) {
            return 12000.00;
        }
        if (params.contains(134) && days == 30) {
            return 1000.00;
        }
        if (params.contains(134) && days == 90) {
            return 3000.00;
        }
        if (params.contains(134) && days == 180) {
            return 6000.00;
        }
        if (params.contains(134) && days == 365) {
            return 12000.00;
        }
        if (params.contains(4) && days == 30) {
            return 1000.00;
        }
        if (params.contains(4) && days == 90) {
            return 3000.00;
        }
        if (params.contains(4) && days == 180) {
            return 6000.00;
        }
        if (params.contains(4) && days == 365) {
            return 12000.00;
        }
        if (params.contains(72) && params.contains(134) && days == 30) {
            return 2000.00;
        }
        if (params.contains(72) && params.contains(134) && days == 90) {
            return 6000.00;
        }
        if (params.contains(72) && params.contains(134) && days == 180) {
            return 12000.00;
        }
        if (params.contains(72) && params.contains(134) && days == 365) {
            return 24000.00;
        }
        if (params.contains(72) && params.contains(4) && days == 30) {
            return 2000.00;
        }
        if (params.contains(72) && params.contains(4) && days == 90) {
            return 6000.00;
        }
        if (params.contains(72) && params.contains(4) && days == 180) {
            return 12000.00;
        }
        if (params.contains(72) && params.contains(4) && days == 365) {
            return 24000.00;
        }
        if (params.contains(4) && params.contains(134) && days == 30) {
            return 2000.00;
        }
        if (params.contains(4) && params.contains(134) && days == 90) {
            return 6000.00;
        }
        if (params.contains(4) && params.contains(134) && days == 180) {
            return 12000.00;
        }
        if (params.contains(4) && params.contains(134) && days == 365) {
            return 24000.00;
        }
        if (params.contains(72) && params.contains(4) && params.contains(134) && days == 30) {
            return 3000.00;
        }
        if (params.contains(72) && params.contains(4) && params.contains(134) && days == 90) {
            return 9000.00;
        }
        if (params.contains(72) && params.contains(4) && params.contains(134) && days == 180) {
            return 18000.00;
        }
        if (params.contains(72) && params.contains(4) && params.contains(134) && days == 365) {
            return 36000.00;
        }
        if (params.contains(84) & params.contains(72) && days == 30) {
            return 6500.00;
        }
        if (params.contains(84) & params.contains(72) && days == 90) {
            return 18000.00;
        }
        if (params.contains(84) & params.contains(72) && days == 180) {
            return 34800.00;
        }
        if (params.contains(84) & params.contains(72) && days == 365) {
            return 66000.00;
        }
        if (params.contains(84) & params.contains(4) && days == 30) {
            return 6500.00;
        }
        if (params.contains(84) & params.contains(4) && days == 90) {
            return 18000.00;
        }
        if (params.contains(84) & params.contains(4) && days == 180) {
            return 348000.00;
        }
        if (params.contains(84) & params.contains(4) && days == 365) {
            return 66000.00;
        }
        if (params.contains(84) & params.contains(134) && days == 30) {
            return 6500.00;
        }
        if (params.contains(84) & params.contains(134) && days == 90) {
            return 18000.00;
        }
        if (params.contains(84) & params.contains(134) && days == 180) {
            return 34800.00;
        }
        if (params.contains(84) & params.contains(134) && days == 365) {
            return 66000.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(134) && days == 30) {
            return 7500.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(134) && days == 90) {
            return 21000.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(134) && days == 180) {
            return 40800.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(134) && days == 365) {
            return 78000.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && days == 30) {
            return 7500.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && days == 90) {
            return 21000.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && days == 180) {
            return 40800.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && days == 365) {
            return 78000.00;
        }
        if (params.contains(84) & params.contains(4) && params.contains(134) && days == 30) {
            return 7500.00;
        }
        if (params.contains(84) & params.contains(4) && params.contains(134) && days == 90) {
            return 21000.00;
        }
        if (params.contains(84) & params.contains(4) && params.contains(134) && days == 180) {
            return 40800.00;
        }
        if (params.contains(84) & params.contains(4) && params.contains(134) && days == 365) {
            return 78000.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && params.contains(134) && days == 30) {
            return 8500.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && params.contains(134) && days == 90) {
            return 24000.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && params.contains(134) && days == 180) {
            return 46800.00;
        }
        if (params.contains(84) & params.contains(72) && params.contains(4) && params.contains(134) && days == 365) {
            return 90000.00;
        }
        return null;
    }

}
