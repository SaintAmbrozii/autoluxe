package com.example.autoluxe.payload.changepass;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserPassRequest {

    @NotBlank
    private String pass;
}
