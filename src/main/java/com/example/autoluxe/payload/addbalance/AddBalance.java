package com.example.autoluxe.payload.addbalance;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AddBalance {

    @NotBlank
    public Double balance;
}
