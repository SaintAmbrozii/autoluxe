package com.example.autoluxe.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BuyAccountResponse {

    private Integer epc_id;

    private String login;

    private String pass;

    private String RFC_expires;
}
