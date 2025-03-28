package com.example.autoluxe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class TokenAuthentificationFilter extends OncePerRequestFilter {

    public static final String HEADER_PREFIX = "Bearer ";

    private TokenProvider tokenProvider;

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public TokenAuthentificationFilter(TokenProvider tokenProvider) {
    }

    public TokenAuthentificationFilter() {

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (StringUtils.hasText(token) && this.tokenProvider.validateToken(token)) {

            UsernamePasswordAuthenticationToken authentication = tokenProvider.getAuthenticationByUserFromDbWithEmail(token);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

      //      Authentication authentication = tokenProvider.getAuthentication(token);
//
    //        UsernamePasswordAuthenticationToken
   //                 usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
   //                 authentication.getPrincipal(), null,
   //                 authentication.getPrincipal() == null ?
   //                         Arrays.asList() : authentication.getAuthorities()
   //         );

    //        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

  ///          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
   ///         filterChain.doFilter(request, response);
   //        return;

        }

        filterChain.doFilter(request, response);

    }

    public void setTokenProvider(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
}
