package com.example.autoluxe.payload.confirmbuy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmByAccountDto {

    private Integer epc_id;

    private String login;

    private String pass;
}
