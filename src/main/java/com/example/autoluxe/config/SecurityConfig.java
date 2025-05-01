package com.example.autoluxe.config;


import com.example.autoluxe.handler.JwtAuthEntryPoint;
import com.example.autoluxe.security.TokenAuthentificationFilter;
import com.example.autoluxe.security.TokenProvider;
import com.example.autoluxe.service.LogoutService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true,securedEnabled = true)
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final LogoutService logoutService;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;


    public SecurityConfig(TokenProvider tokenProvider,
                          @Lazy LogoutService logoutService, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.tokenProvider = tokenProvider;
        this.logoutService = logoutService;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    @Bean
    public TokenAuthentificationFilter tokenAuthenticationFilter() {
        TokenAuthentificationFilter tokenAuthenticationFilter = new TokenAuthentificationFilter();
        tokenAuthenticationFilter.setTokenProvider(tokenProvider);
        return tokenAuthenticationFilter;
    }



    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration configuration
    ) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity)
            throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->corsConfigurationSource())
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.UNAUTHORIZED
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        }))

                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/api/auth/**")
                                .permitAll()
                                .requestMatchers("/","/contact","/favicon.ico")
                                .permitAll()
                                .requestMatchers("/swagger-ui/**")
                                .permitAll()
                                .requestMatchers("/v3/api-docs/**")
                                .permitAll()
                                .requestMatchers("/api/users/**")
                                .hasAnyAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/profile/**")
                                .hasAnyAuthority("ROLE_ADMIN","ROLE_USER")
                                .requestMatchers("/api/accounts/**")
                                .hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/payments/**")
                                .hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/calculation/**")
                                .hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/contact/**")
                                .hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/messages/**")
                                .hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated())
                .logout(logout->logout.addLogoutHandler(logoutService)
                        .logoutUrl("api/users/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
                .anonymous(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->exception.authenticationEntryPoint(jwtAuthEntryPoint))
                .addFilterBefore(tokenAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
