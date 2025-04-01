package com.example.autoluxe.payload;

import com.example.autoluxe.utils.validator.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @ValidEmail
    private String email;
    @NotBlank
    private String password;
}
