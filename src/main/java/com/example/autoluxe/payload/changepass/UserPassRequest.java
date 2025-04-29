package com.example.autoluxe.payload.changepass;

import com.example.autoluxe.utils.validator.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPassRequest {

    @NullOrNotBlank(message = "Пароль не должно быть пустым.")
    private String pass;
}
