package com.example.autoluxe.service;


import com.example.autoluxe.domain.Token;
import com.example.autoluxe.repo.TokenRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class LogoutService implements LogoutHandler {

    private final TokenRepo tokenRepository;

    public LogoutService(TokenRepo tokenRepository) {
        this.tokenRepository = tokenRepository;
    }


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            Optional<Token> userToken = tokenRepository.findByToken(jwt);
            if (userToken.isPresent()) {
                Token existedToken = userToken.get();
                existedToken.setExpired(true);
                existedToken.setRevoked(true);
                tokenRepository.save(existedToken);

            }
        }

    }
}

