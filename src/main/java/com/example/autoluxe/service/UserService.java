package com.example.autoluxe.service;


import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.AdminDto;
import com.example.autoluxe.dto.UserDto;
import com.example.autoluxe.exception.NotFoundException;
import com.example.autoluxe.payload.*;
import com.example.autoluxe.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final String EPIC_URI = "https://epcinfo.ru/api/v2/";

    private static final String partner_token = "1e3972f9908c713356e9eb255947f148";

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;

    public UserService(UserRepo userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
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

    public UserDto getUserToken (Long userId,User user) {

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

        User updated = userRepo.save(inDB);
        return UserDto.toDto(updated);

    }

    public UserDto addSubUser(Long userId) {

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

        List<String> epicId = Collections.singletonList(String.valueOf(response.getEpc_id()));
        inDB.setEpics_ids(epicId);

        User update = userRepo.save(inDB);

        return UserDto.toDto(update);

    }

    public ResponseEntity<?> hideAccount(Long userId) {
        User inDB = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        RestClient client = RestClient.create();

        HideAccRequest request = HideAccRequest.builder()
                .token(inDB.getEpic_token())
                .epic_ids(inDB.getEpics_ids().toArray(new String[0])).build();

        client.
                post().
                uri(EPIC_URI + "hide_acc").
                body(request).
                retrieve()
                .toBodilessEntity();
        return (ResponseEntity<?>) ResponseEntity.status(200);
    }



    public AdminDto addPartnerToken(User user, String token) {
        User inDb = userRepo.findById(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found"));
        inDb.setPartner_token(token);

        User update = userRepo.save(inDb);
        return AdminDto.toDto(user);
    }



    public UserDto findById(Long id) {
        User inDB = userRepo.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
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
