package com.example.autoluxe.payload.changename;

import com.example.autoluxe.utils.validator.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserNameRequest {

    @NullOrNotBlank(message = "Имя не должно быть пустым.")
    private String name;
}
