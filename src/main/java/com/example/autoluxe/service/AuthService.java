package com.example.autoluxe.service;

import com.example.autoluxe.config.AppProperties;
import com.example.autoluxe.domain.Role;
import com.example.autoluxe.domain.Token;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.VerificationToken;
import com.example.autoluxe.events.*;
import com.example.autoluxe.exception.AppException;
import com.example.autoluxe.payload.auth.ApiResponse;
import com.example.autoluxe.payload.auth.LoginRequest;
import com.example.autoluxe.payload.auth.LoginResponse;
import com.example.autoluxe.payload.auth.SignUpRequest;
import com.example.autoluxe.payload.jwt.TokenResponse;
import com.example.autoluxe.repo.UserRepo;
import com.example.autoluxe.security.TokenProvider;
import com.example.autoluxe.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepo userRepo;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
    private final UserTokenService tokenService;
    private final CookieUtil cookieUtil;
    private final AppProperties properties;
    private final GetUserTokenListener getUserTokenListener;
    private final GetUserAccountsListener getUserAccountsListener;
    private final AccountService accountService;
    private final RegistrationListener registrationListener;

    public AuthService(AuthenticationManager authenticationManager, UserService userService,
                       UserRepo userRepo, TokenProvider tokenProvider,
                       PasswordEncoder encoder, UserTokenService tokenService,
                       CookieUtil cookieUtil, AppProperties properties,
                       GetUserTokenListener getUserTokenListener,
                       GetUserAccountsListener getUserAccountsListener, AccountService accountService,
                       RegistrationListener registrationListener) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepo = userRepo;
        this.tokenProvider = tokenProvider;
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.cookieUtil = cookieUtil;
        this.properties = properties;
        this.getUserTokenListener = getUserTokenListener;
        this.getUserAccountsListener = getUserAccountsListener;
        this.accountService = accountService;
        this.registrationListener = registrationListener;
    }


    public ResponseEntity<TokenResponse> authenticateUser(LoginRequest loginRequest,
                                                          String accessToken,
                                                          String refreshToken) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        HttpHeaders responseHeaders = new HttpHeaders();

        final User user = userRepo.findUserByEmail(loginRequest.getEmail()).orElseThrow();
        tokenService.revokeAllUserToken(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String access = tokenProvider.createToken(user);
        String refresh = tokenProvider.createRefreshToken(user);
        TokenResponse tokenResponse = TokenResponse.builder().accessToken(access).refreshToken(refresh).build();
        tokenService.saveToken(user, refresh);

        getUserAccountsListener.onApplicationEvent(new GetUserAccountsEvent(user.getId()));

        addAccessTokenCookie(responseHeaders, accessToken);
        addRefreshTokenCookie(responseHeaders, refreshToken);

        return ResponseEntity.ok().headers(responseHeaders).body(tokenResponse);
    }


    public ResponseEntity<ApiResponse> registerUser(SignUpRequest signUpRequest,
                                                    HttpServletRequest request) {
        Optional<User> userFromDB = userRepo.findUserByEmail(signUpRequest.getEmail());

        String appUrl = request.getContextPath();

        if (userFromDB.isPresent()) {
            throw new BadCredentialsException("Username is already exists");
        }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());

        user.setRole(Role.ROLE_ADMIN);

        User userAfterSaving = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(userAfterSaving.getId()).toUri();

        getUserTokenListener.onApplicationEvent(new GetUserTokenEvent(user.getId()));

        registrationListener.onApplicationEvent(new RegistrationCompleteEvent(userAfterSaving,appUrl,request.getLocale()));

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }

    public ResponseEntity<ApiResponse> confirm(String token) {

        VerificationToken verificationToken = accountService.getVerificationToken(token);

        User user = accountService.getUser(verificationToken.getToken());

        user.setActive(true);
        userRepo.save(user);

        return
                ResponseEntity.ok(new ApiResponse(true, "User confirmed successfully!"));
    }

    public ResponseEntity<TokenResponse> getRefreshToken(String refresh) {

        boolean refreshTokenValid = tokenProvider.validateToken(refresh);

        if(!refreshTokenValid)
            throw new AppException(HttpStatus.BAD_REQUEST, "Refresh token is invalid");
        Optional<Token> inDb = tokenService.findByToken(refresh);

        Token currentToken = inDb.get();
        if (Date.from(Instant.now()).after(currentToken.getDuration())) {
            tokenService.deleteToken(currentToken.getId());
        }

        String username = tokenProvider.getUserEmailFromToken(refresh);
        User user = userRepo.findUserByEmail(username).orElseThrow();

        String refreshToken = currentToken.getToken();
        String newAccessToken = tokenProvider.createToken(user);

        TokenResponse response = TokenResponse.builder()
                .refreshToken(refreshToken).accessToken(newAccessToken).accessToken(newAccessToken).build();

        HttpHeaders responseHeaders = new HttpHeaders();
        addAccessTokenCookie(responseHeaders, newAccessToken);

        return ResponseEntity.ok().headers(responseHeaders).body(response);
    }

    public ResponseEntity<LoginResponse> logout(String accessToken, String refreshToken) {

        HttpHeaders responseHeaders = new HttpHeaders();

        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie().toString());
        responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteRefreshTokenCookie().toString());

        LoginResponse loginResponse = LoginResponse.builder()
                .isLogged(false)
                .role(null).build();

        return ResponseEntity.ok().headers(responseHeaders).body(loginResponse);
    }





    private void addAccessTokenCookie(HttpHeaders httpHeaders, String token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token, properties.getAuth().getTokenExpirationMsec()).toString());
    }
    private void addRefreshTokenCookie(HttpHeaders httpHeaders, String token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token, properties.getAuth().getRefreshExpirationMsec()).toString());
    }

}


