package com.example.autoluxe.payload.getbuytoken;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
public class BuyTokenRequest {


    private List<Integer> param;

    private Integer days;
}
