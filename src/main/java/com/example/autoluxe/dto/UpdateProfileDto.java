package com.example.autoluxe.dto;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import com.example.autoluxe.utils.validator.NullOrNotBlank;
import com.example.autoluxe.utils.validator.ValidEmail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateProfileDto {


    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;
}
