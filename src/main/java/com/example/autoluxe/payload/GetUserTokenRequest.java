package com.example.autoluxe.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserTokenRequest {

    private String partner_token;

    private Long user_id;
}
