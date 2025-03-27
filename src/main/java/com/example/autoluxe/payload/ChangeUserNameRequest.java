package com.example.autoluxe.payload;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeUserNameRequest {

    private String token;

    private String epc_id;

    private String name;
}
