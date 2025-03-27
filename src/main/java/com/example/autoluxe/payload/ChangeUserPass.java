package com.example.autoluxe.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserPass {

    private String token;

    private String epc_id;

    private String pass;
}
