package com.example.autoluxe.payload;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class LoginResponse {

   private boolean isLogged;
   private String role;
}
