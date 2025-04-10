package com.example.autoluxe.controller;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.dto.UserAccountDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.payload.changelogin.UserLoginRequest;
import com.example.autoluxe.payload.changename.UserNameRequest;
import com.example.autoluxe.payload.changepass.ChangeUserPass;
import com.example.autoluxe.payload.getbuytoken.BuyTokenRequest;
import com.example.autoluxe.service.AccountService;
import com.example.autoluxe.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/profile")
public class ProfileController {

    private final UserService userService;
    private final AccountService accountService;

    public ProfileController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @GetMapping
    public UserDto profile(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }

    @GetMapping("/accounts")
    public List<UserAccountDto> findByUser(@AuthenticationPrincipal User user) {
        return accountService.findAllByUserId(user.getId());
    }
    @PatchMapping
    public UserDto update (@AuthenticationPrincipal User user) {
        return userService.updateUser(user.getId(), user);
    }

    @PatchMapping("/accounts/hide/{id}")
    public void hide (@PathVariable(name = "id") Long accountId,
                                             @AuthenticationPrincipal User user) {
        userService.hideAccount(accountId,user.getId());
    }
    @PatchMapping("/accounts/changename/{id}")
    public UserAccount changeName(@PathVariable(name = "id")Long accountId,
                                                  @AuthenticationPrincipal User user,
                                                  @RequestBody UserNameRequest request) {

       return  userService.changeUserName(user.getId(),accountId,request);
    }
    @PatchMapping("/accounts/changelogin/{id}")
    public UserAccount changeLogin(@PathVariable(name = "id")Long accountId,
                                                   @AuthenticationPrincipal User user,
                                                   @RequestBody UserLoginRequest request) {
       return   userService.changeUserLogin(user.getId(),accountId,request);
    }
    @PatchMapping("/accounts/changepass/{id}")
    public UserAccount changePass(@PathVariable(name = "id")Long accountId,
                                                  @AuthenticationPrincipal User user,
                                                  @RequestBody ChangeUserPass request) {
        return userService.changeUserPass(user.getId(),accountId,request);
    }

    @PatchMapping("/addsub")
    public void addSubUser(@AuthenticationPrincipal User user) {
        userService.addSubUser(user.getId());
    }

    @PatchMapping(value = "/accounts/buy/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    public void buyAccount(@PathVariable(name = "id") Long id,
                           @AuthenticationPrincipal User user,
                           @RequestBody BuyTokenRequest request) {
        userService.getByToken(user.getId(), id,request);

    }



}
