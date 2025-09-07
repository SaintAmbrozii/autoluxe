package com.example.autoluxe.controller;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.dto.UserAccountDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.dto.UserProfileDto;
import com.example.autoluxe.payload.auth.SignUpRequest;
import com.example.autoluxe.payload.changelogin.UserLoginRequest;
import com.example.autoluxe.payload.changename.UserNameRequest;
import com.example.autoluxe.payload.changepass.ChangeUserPass;
import com.example.autoluxe.payload.getbuytoken.BuyTokenRequest;
import com.example.autoluxe.service.AccountService;
import com.example.autoluxe.service.UserService;
import jakarta.validation.Valid;
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
    public UserProfileDto profile(@AuthenticationPrincipal User user) {
        return userService.findByProfile(user.getId());
    }

    @GetMapping("/accounts")
    public List<UserAccountDto> findByUser(@AuthenticationPrincipal User user) {
        return accountService.findAllByUserId(user.getId());
    }
    @PatchMapping
    public UserProfileDto update (@AuthenticationPrincipal User user,
                                  @RequestBody UserProfileDto request) {
        return userService.updateProfile(user,request);
    }

    @PatchMapping("/accounts/hide/{id}")
    public void hide (@PathVariable(name = "id") Long accountId,
                                             @AuthenticationPrincipal User user) {
        userService.hideAccount(accountId,user.getId());
    }
    @PatchMapping("/accounts/changename/{id}")
    public UserAccountDto changeName(@PathVariable(name = "id")Long accountId,
                                                  @AuthenticationPrincipal User user,
                                                  @RequestBody @Valid UserNameRequest request) {

       return  userService.changeUserName(user.getId(),accountId,request);
    }
    @PatchMapping("/accounts/changelogin/{id}")
    public UserAccountDto changeLogin(@PathVariable(name = "id")Long accountId,
                                                   @AuthenticationPrincipal User user,
                                                   @RequestBody @Valid UserLoginRequest request) {
       return   userService.changeUserLogin(user.getId(),accountId,request);
    }
    @PatchMapping("/accounts/changepass/{id}")
    public UserAccountDto changePass(@PathVariable(name = "id")Long accountId,
                                                  @AuthenticationPrincipal User user,
                                                  @RequestBody @Valid ChangeUserPass request) {
        return userService.changeUserPass(user.getId(),accountId,request);
    }

    @PatchMapping("/addsub")
    public UserAccountDto addSubUser(@AuthenticationPrincipal User user) {
        return userService.addSubUser(user.getId());
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
