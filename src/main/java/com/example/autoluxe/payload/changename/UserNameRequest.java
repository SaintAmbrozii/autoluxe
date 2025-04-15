package com.example.autoluxe.payload.changename;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserNameRequest {

    @NotBlank
    String name;
}
