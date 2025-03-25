package com.example.autoluxe.controller;



import com.example.autoluxe.domain.Role;
import com.example.autoluxe.domain.Token;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.payload.ApiResponse;
import com.example.autoluxe.payload.LoginRequest;
import com.example.autoluxe.payload.SignUpRequest;
import com.example.autoluxe.payload.TokenResponse;
import com.example.autoluxe.repo.UserRepo;
import com.example.autoluxe.security.TokenProvider;
import com.example.autoluxe.service.LogoutService;
import com.example.autoluxe.service.UserService;
import com.example.autoluxe.service.UserTokenService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepo userRepo;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
    private final UserTokenService tokenService;
    private final LogoutService logoutService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService,
                          UserRepo userRepo, TokenProvider tokenProvider, PasswordEncoder encoder,
                          UserTokenService tokenService, LogoutService logoutService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepo = userRepo;
        this.tokenProvider = tokenProvider;
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.logoutService = logoutService;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        final User user = userRepo.findUserByEmail(loginRequest.getEmail()).orElseThrow();
        tokenService.revokeAllUserToken(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);
        TokenResponse tokenResponse = TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        tokenService.saveToken(user, refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpRequest signUpRequest) {
        Optional<User> userFromDB = userRepo.findUserByEmail(signUpRequest.getEmail());

        if (userFromDB.isPresent()) {
            throw new BadCredentialsException("Username is already exists");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());

        user.setRole(Role.ROLE_ADMIN);

        User userAfterSaving = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(userAfterSaving.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }



    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> getRefreshToken(@RequestBody String token) {
        Optional<Token> inDb = tokenService.findByToken(token);
        Token currentToken = inDb.get();
        if (Date.from(Instant.now()).after(currentToken.getDuration())) {
            tokenService.deleteToken(currentToken.getId());
        }
        String refreshToken = currentToken.getToken();
        TokenResponse response = TokenResponse.builder().refreshToken(refreshToken).accessToken(null).build();
        return ResponseEntity.ok(response);
    }



}
