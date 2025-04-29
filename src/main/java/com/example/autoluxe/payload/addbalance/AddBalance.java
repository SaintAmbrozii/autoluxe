package com.example.autoluxe.payload.addbalance;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class AddBalance {

    @NotNull(message = "Сумма не должна быть null.")
    @Positive(message = "Сумма должна быть > 0.")
    public Double balance;
}
