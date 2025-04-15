package com.example.autoluxe.payload.changelogin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank
    private String login;
}
