package com.example.autoluxe.payload.getbuytoken;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
public class BuyTokenRequest {

    private List<Integer> param;

    private Integer days;
}
