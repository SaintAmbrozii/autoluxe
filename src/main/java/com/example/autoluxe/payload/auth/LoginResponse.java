package com.example.autoluxe.payload.auth;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginResponse {

   private boolean isLogged;
   private String role;
}
