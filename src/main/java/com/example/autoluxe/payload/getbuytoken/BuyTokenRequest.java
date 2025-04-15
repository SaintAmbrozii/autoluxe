package com.example.autoluxe.payload.getbuytoken;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
public class BuyTokenRequest {

    @NotBlank
    private List<Integer> param;

    @NotBlank
    private Integer days;
}
