package com.example.autoluxe.controller;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.AdminDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.repo.UserRepo;
import com.example.autoluxe.service.UserService;
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

    @PatchMapping("addsub/{id}")
    public UserDto addSubUser(@PathVariable(name = "id")Long id) {
        return userService.addSubUser(id);
    }

    @PatchMapping("adminToken")
    public AdminDto addPartnerToken(@AuthenticationPrincipal User user,@RequestBody String token) {
        return userService.addPartnerToken(user,token);
    }

    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable(name = "id")Long id){
        userService.deleteUser(id);
    }
}
