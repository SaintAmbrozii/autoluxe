package com.example.autoluxe.payload.auth;

import com.example.autoluxe.utils.validator.NullOrNotBlank;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PasswordResetDto {

    @NotBlank
    private String token;

    @NullOrNotBlank(message = "Пароль не должно быть пустым.")
    private String password;

    @NullOrNotBlank(message = "Пароль не должно быть пустым.")
    private String confirmPassword;
}
