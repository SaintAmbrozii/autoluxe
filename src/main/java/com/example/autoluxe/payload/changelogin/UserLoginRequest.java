package com.example.autoluxe.payload.changelogin;

import com.example.autoluxe.utils.validator.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginRequest {

    @NullOrNotBlank(message = "Логин не должно быть пустым.")
    private String login;
}
