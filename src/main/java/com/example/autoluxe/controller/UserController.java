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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<UserDto> list(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                              @RequestParam(value = "count", defaultValue = "50", required = false) int size,
                              @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                              @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty) {
        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepo.findAll(pageable).map(UserDto::toDto);
    }

 //   @GetMapping
  //  public List<UserDto> findAll() {
 //       return userService.getAllUsers();
  //  }

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


    @DeleteMapping("{id}")
    public void deleteUser(@PathVariable(name = "id")Long id){
        userService.deleteUser(id);
    }
}
