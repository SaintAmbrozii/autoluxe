package com.example.autoluxe.controller;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDto profile(@AuthenticationPrincipal User user) {
        return userService.findById(user.getId());
    }
}
