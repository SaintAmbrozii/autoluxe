package com.example.autoluxe.payload.confirmbuy;

import com.example.autoluxe.utils.DateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConfirmByAccountDto {


    private Integer epc_id;

    private String login;

    private String pass;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime expires;
}
