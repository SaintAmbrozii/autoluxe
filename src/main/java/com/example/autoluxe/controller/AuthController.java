package com.example.autoluxe.controller;



import com.example.autoluxe.domain.*;
import com.example.autoluxe.events.*;
import com.example.autoluxe.exception.AppException;
import com.example.autoluxe.payload.auth.*;
import com.example.autoluxe.payload.jwt.TokenResponse;
import com.example.autoluxe.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> authenticateUser(@RequestBody @Valid LoginRequest loginRequest,
                                                          @CookieValue(name = "access_token", required = false) String accessToken,
                                                          @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        return authService.authenticateUser(loginRequest, accessToken, refreshToken);
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody
                                                        @Valid SignUpRequest signUpRequest,
                                                    HttpServletRequest request) {
        return authService.registerUser(signUpRequest, request);
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<ApiResponse> confirm(@RequestParam("token") String token) {

        return authService.confirm(token);
    }


    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> getRefreshToken(@CookieValue(name = "refresh_token", required = true) String refresh) {

        return authService.getRefreshToken(refresh);
    }

    @PostMapping("/logout")
    public ResponseEntity<LoginResponse> logout(
            @CookieValue(name = "access_token", required = false) String accessToken,
            @CookieValue(name = "refresh_token", required = false) String refreshToken) {

        return authService.logout(accessToken,refreshToken);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam("email") String email) {
        return authService.resetPassword(email);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(Model model,@RequestParam("token")String token) {
        return authService.changePassword(model, token);

    }

    @PostMapping("/savePassword")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid PasswordResetDto passwordResetDto) {
        return authService.handlePasswordReset(passwordResetDto);
    }



}
