package com.example.autoluxe.dto;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import com.example.autoluxe.utils.validator.ValidEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageDto {

    @NotBlank
    private String name;
    @ContactNumberConstraint
    @NotBlank
    private String phone;
    @ValidEmail
    @NotBlank
    private String email;
    @NotBlank
    private String message;
    @NotBlank
    private String vin;

    private String frame;
    @NotBlank
    private String model;
    @NotBlank
    private String detail_name;
}
