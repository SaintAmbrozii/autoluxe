package com.example.autoluxe.payload.getbuytoken;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BuyTokenRequest {

    private String param;

    private Integer days;
}
