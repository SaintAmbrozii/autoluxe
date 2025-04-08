package com.example.autoluxe.payload.getbuytoken;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class BuyTokenRequest {

    private Integer param;

    private Integer days;
}
