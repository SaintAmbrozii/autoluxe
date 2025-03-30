package com.example.autoluxe.controller;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.AdminDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.payload.*;
import com.example.autoluxe.payload.confirmbuy.ConfirmByResponse;
import com.example.autoluxe.payload.getbuytoken.GetByTokenRequest;
import com.example.autoluxe.payload.getbuytoken.GetByTokenResponse;
import com.example.autoluxe.payload.getuseraccounts.GetUserAccountResponse;
import com.example.autoluxe.repo.UserRepo;
import com.example.autoluxe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;

    public UserController(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable(name = "id")Long id) {
        return userService.findById(id);
    }

    @GetMapping("/profile")
    public AdminDto profile(@AuthenticationPrincipal User user) {
        return userService.getProfile(user.getId());
    }

    @PatchMapping("{id}")
    public UserDto updateUser(@PathVariable(name = "id")Long id, @AuthenticationPrincipal User user) {
        return userService.updateUser(id, user);
    }
    @PatchMapping("gettoken/{id}")
    public UserDto getUserToken(@PathVariable(name = "id")Long id,@AuthenticationPrincipal User user) {
        return userService.getUserToken(id, user);
    }
    @PatchMapping("{id}/hideacc/{accId}")
    public ResponseEntity<ApiResponse> hideAcc(@PathVariable(name = "id")Long id,

                                               @PathVariable(name = "accId")Long accId) {
       return userService.hideAccount(id,accId);
    }
    @PatchMapping("{id}/chageName/{accId}")
    public ResponseEntity<ApiResponse> changeName(@PathVariable(name = "id")Long id,
                                                  @PathVariable(name = "accId") Long accountId,
                                                  @RequestBody ChangeUserNameRequest request) {
        return userService.changeUserName(id,accountId, request);
    }
    @PatchMapping("{id}/changeLogin/{accId}")
    public ResponseEntity<ApiResponse> changeLogin(@PathVariable(name = "id")Long id,
                                                   @PathVariable(name = "accId") Long accountId,
                                                   @RequestBody ChangeUserLoginRequest request) {
        return userService.changeUserLogin(id,accountId, request);
    }
    @PatchMapping("{id}/changePass/{accId}")
    public ResponseEntity<ApiResponse> changePass(@PathVariable(name = "id")Long id,
                                                  @PathVariable(name = "accId") Long accountId,
                                                  @RequestBody ChangeUserPass request) {
        return userService.changeUserPass(id,accountId, request);
    }
    @PatchMapping("/{id}/getByToken/{accId}")
    public ResponseEntity<GetByTokenResponse> getByToken(@PathVariable(name = "id")Long id,
                                                         @PathVariable(name = "accId") Long accId,
                                                         @RequestBody GetByTokenRequest request) {
        return userService.getByToken(id,accId,request);
    }

    @PatchMapping("{id}/confirmBuy")
    public ResponseEntity<ConfirmByResponse> confirmBy(@PathVariable(name = "id")Long id) {
        return userService.confirmBuy(id);
    }

    @PatchMapping("{id}/getUserAcc")
    public ResponseEntity<GetUserAccountResponse> getUserAccounts(@PathVariable(name = "id")Long id) {
        return userService.getUserAccount(id);
    }


    @PatchMapping("addsub/{id}")
    public UserDto addSubUser(@PathVariable(name = "id")Long id) {
        return userService.addSubUser(id);
    }

    @PatchMapping("admintoken")
    public AdminDto addPartnerToken(@AuthenticationPrincipal User user,
                                    @RequestBody AddPartnerToken token) {
        return userService.addPartnerToken(user,token);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable(name = "id")Long id){
        userService.deleteUser(id);
    }
}
