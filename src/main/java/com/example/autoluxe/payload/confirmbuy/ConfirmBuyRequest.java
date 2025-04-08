package com.example.autoluxe.payload.confirmbuy;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConfirmBuyRequest {

    @JsonProperty("token")
    private String token;

    @JsonProperty("Btoken")
    private String btoken;
}
