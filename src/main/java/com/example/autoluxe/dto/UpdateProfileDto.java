package com.example.autoluxe.dto;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import com.example.autoluxe.utils.validator.NullOrNotBlank;
import com.example.autoluxe.utils.validator.ValidEmail;
import lombok.Data;

@Data
public class UpdateProfileDto {

    @ValidEmail
    private String email;
    @NullOrNotBlank(message = "Пароль не должно быть пустым.")
    private String password;
    @NullOrNotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @ContactNumberConstraint
    private String phone;
}
