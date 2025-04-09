package com.example.autoluxe.payload.changelogin;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeUserLoginRequest {

    private String token;

    private String epc_id;

    private String login;
}
