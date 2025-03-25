package com.example.autoluxe.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenResponse {

    private String accessToken;
    private String refreshToken;
    private final String tokenType = "Bearer";



}
