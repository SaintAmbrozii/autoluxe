package com.example.autoluxe.payload.getbuytoken;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetByTokenRequest {

    private String token;

    private String user_ids;

    private String products;

    private Integer days;
}
