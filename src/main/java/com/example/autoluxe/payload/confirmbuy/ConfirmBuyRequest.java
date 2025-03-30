package com.example.autoluxe.payload.confirmbuy;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmBuyRequest {

    private String token;

    private String Btoken;
}
