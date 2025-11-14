package com.example.autoluxe.service;

import com.example.autoluxe.config.AppProperties;
import com.example.autoluxe.domain.*;
import com.example.autoluxe.events.*;
import com.example.autoluxe.exception.AppException;
import com.example.autoluxe.exception.AutoluxeException;
import com.example.autoluxe.payload.auth.*;
import com.example.autoluxe.payload.jwt.TokenResponse;
import com.example.autoluxe.repo.UserRepo;
import com.example.autoluxe.security.TokenProvider;
import com.example.autoluxe.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

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
    private final PasswordResetTokenService passwordResetTokenService;
    private final ResetPasswordListener resetPasswordListener;

    @Value("${BASIC_URL}")
    private String basicUrl;

    public AuthService(AuthenticationManager authenticationManager, UserService userService,
                       UserRepo userRepo, TokenProvider tokenProvider,
                       PasswordEncoder encoder, UserTokenService tokenService,
                       CookieUtil cookieUtil, AppProperties properties,
                       GetUserTokenListener getUserTokenListener,
                       GetUserAccountsListener getUserAccountsListener, AccountService accountService,
                       RegistrationListener registrationListener, PasswordResetTokenService passwordResetTokenService,
                       ResetPasswordListener resetPasswordListener) {
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
        this.passwordResetTokenService = passwordResetTokenService;
        this.resetPasswordListener = resetPasswordListener;
    }

    @ModelAttribute("passwordResetForm")
    public PasswordResetDto passwordReset() {
        return new PasswordResetDto();
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

        addAccessTokenCookie(responseHeaders, access);
        addRefreshTokenCookie(responseHeaders, refresh);

        return ResponseEntity.ok().headers(responseHeaders).body(tokenResponse);
    }


    public ResponseEntity<ApiResponse> registerUser(SignUpRequest signUpRequest,
                                                    HttpServletRequest request) {

        String appUrl = request.getContextPath();

     //   if (userService.doesUsernameExists(signUpRequest.getName())) {
    //        throw new BadCredentialsException("Username is already exists");
    //    }

        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setName(signUpRequest.getName());

        user.setRole(Role.ROLE_USER);
        user.setBalance(BigDecimal.valueOf(0.00));

        User userAfterSaving = userService.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(userAfterSaving.getId()).toUri();

        getUserTokenListener.onApplicationEvent(new GetUserTokenEvent(user.getId()));

        registrationListener.onApplicationEvent(new RegistrationCompleteEvent(userAfterSaving));

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully!"));
    }

    public ResponseEntity<ApiResponse> confirm(String token) {

        VerificationToken verificationToken = accountService.getVerificationToken(token);

        if (verificationToken!=null) {

            User user = accountService.getUser(verificationToken.getToken());

            user.setActive(true);
            userRepo.save(user);

            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(basicUrl + "/registrationSuccess"));

            return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }

        return ResponseEntity.ok(new ApiResponse(true, "User confirmed successfully!"));
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

    public ResponseEntity<ApiResponse> resetPassword(String email) {

        Optional<User> inDb = userRepo.findUserByEmail(email);
        if (inDb.isPresent()) {
            User user = inDb.get();
            String token = UUID.randomUUID().toString();
            passwordResetTokenService.createPasswordResetTokenForUser(user,token);

            resetPasswordListener.onApplicationEvent(new ResetPasswordEvent(user,token));

        } else {
            var msg = "Email not found";
            throw new AutoluxeException(msg,HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true,"Reset password sucessfully!"));

    }

    public ResponseEntity<String> changePassword(Model model,String token) {

        String result = passwordResetTokenService.validatePasswordResetToken(token);
        System.out.println(result);
        if (result !=null) {
            model.addAttribute("token",token);
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create(basicUrl + "/savePassword"));


           return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
        }
       return ResponseEntity.ok("Not token exception");
    }

    @Transactional
    public ResponseEntity<String> handlePasswordReset(@ModelAttribute("passwordResetForm")
                                                           @Valid PasswordResetDto resetDto) {

        PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(resetDto.getToken());

        User user = passwordResetToken.getUser();
        if (resetDto.getPassword().equals(resetDto.getConfirmPassword())) {
            String updatedPassword = resetDto.getPassword();
            userService.changeUserPassword(user,updatedPassword);

            passwordResetTokenService.delete(passwordResetToken);
        }

        return ResponseEntity.ok("Пароль пользователя успешно обновлен");

    }



    private void addAccessTokenCookie(HttpHeaders httpHeaders, String token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createAccessTokenCookie(token, properties.getAuth().getTokenExpirationMsec()).toString());
    }
    private void addRefreshTokenCookie(HttpHeaders httpHeaders, String token) {
        httpHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.createRefreshTokenCookie(token, properties.getAuth().getRefreshExpirationMsec()).toString());
    }

}


