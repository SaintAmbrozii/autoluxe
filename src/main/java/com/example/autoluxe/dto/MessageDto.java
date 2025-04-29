package com.example.autoluxe.dto;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import com.example.autoluxe.utils.validator.NullOrNotBlank;
import com.example.autoluxe.utils.validator.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageDto {

    @NotBlank
    private String name;
    @NotBlank
    @ContactNumberConstraint
    private String phone;
    @NotBlank
    @ValidEmail
    private String email;
    @NullOrNotBlank(message = "Сообщение не должно быть пустым.")
    private String message;
    @NullOrNotBlank(message = "Вин не должно быть пустым.")
    private String vin;

    private String frame;
    @NullOrNotBlank(message = "Модель не должно быть пустым.")
    private String model;
    @NullOrNotBlank(message = "Имя детали не должно быть пустым.")
    private String detail_name;
}
