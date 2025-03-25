package com.example.autoluxe.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HideAccRequest {

    private String token;

    private String [] epic_ids;
}
