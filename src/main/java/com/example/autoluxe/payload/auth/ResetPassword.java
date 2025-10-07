package com.example.autoluxe.payload.auth;

import com.example.autoluxe.utils.validator.ValidEmail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResetPassword {

    @ValidEmail
    private String email;
}
