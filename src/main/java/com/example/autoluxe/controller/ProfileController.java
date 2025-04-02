package com.example.autoluxe.controller;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.payload.ApiResponse;
import com.example.autoluxe.payload.ChangeUserLoginRequest;
import com.example.autoluxe.payload.ChangeUserNameRequest;
import com.example.autoluxe.payload.ChangeUserPass;
import com.example.autoluxe.payload.getbuytoken.GetByTokenRequest;
import com.example.autoluxe.service.AccountService;
import com.example.autoluxe.service.UserService;
import org.springframework.http.ResponseEntity;
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
    public List<UserAccount> findByUser(@AuthenticationPrincipal User user) {
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
    @PatchMapping("/accounts/changeName/{id}")
    public void changeName(@PathVariable(name = "id")Long accountId,
                                                  @AuthenticationPrincipal User user,
                                                  @RequestBody ChangeUserNameRequest request) {

         userService.changeUserName(user.getId(),accountId,request);
    }
    @PatchMapping("/accounts/changeLogin/{id}")
    public void changeLogin(@PathVariable(name = "id")Long accountId,
                                                   @AuthenticationPrincipal User user,
                                                   @RequestBody ChangeUserLoginRequest request) {
         userService.changeUserLogin(user.getId(),accountId,request);
    }
    @PatchMapping("/accounts/changePass/{id}")
    public void changePass(@PathVariable(name = "id")Long accountId,
                                                  @AuthenticationPrincipal User user,
                                                  @RequestBody ChangeUserPass request) {
         userService.changeUserPass(user.getId(),accountId,request);
    }

    @PatchMapping("/addsub")
    public void addSubUser(@AuthenticationPrincipal User user) {
        userService.addSubUser(user.getId());
    }

    @PatchMapping("/accounts/buy/{id}")
    public void buyAccount(@PathVariable(name = "id") Long id,
                           @AuthenticationPrincipal User user,
                           @RequestBody GetByTokenRequest request) {
        userService.getByToken(user.getId(), id,request);

    }



}
