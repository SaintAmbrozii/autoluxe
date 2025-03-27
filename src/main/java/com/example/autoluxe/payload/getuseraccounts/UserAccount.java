package com.example.autoluxe.payload.getuseraccounts;

import com.example.autoluxe.utils.BooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccount {

    private Integer id;

    private String name;

    @JsonProperty
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean hide;

    private String AD_name;

    private String pass;

    private Integer status;

    private String RFC_expires;


}
