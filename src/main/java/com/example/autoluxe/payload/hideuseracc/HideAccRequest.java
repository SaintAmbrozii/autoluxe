package com.example.autoluxe.payload.hideuseracc;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HideAccRequest {

    private String token;

    private String epc_ids;
}
