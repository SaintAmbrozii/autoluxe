package com.example.autoluxe.payload.auth;

import com.example.autoluxe.utils.validator.NullOrNotBlank;
import com.example.autoluxe.utils.validator.ValidEmail;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginRequest {


    @ValidEmail
    private String email;
    @NotBlank(message = "Пароль не должно быть пустым.")
    private String password;
}
