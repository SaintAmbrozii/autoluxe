package com.example.autoluxe.payload.auth;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import com.example.autoluxe.utils.validator.NullOrNotBlank;
import com.example.autoluxe.utils.validator.ValidEmail;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequest {

    @NullOrNotBlank(message = "Почта не должно быть пустой.")
    @ValidEmail
    private String email;
    @NullOrNotBlank(message = "Пароль не должно быть пустым.")
    private String password;
    @NullOrNotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @NullOrNotBlank(message = "Телефон не должно быть пустым.")
    @ContactNumberConstraint
    private String phone;

}
