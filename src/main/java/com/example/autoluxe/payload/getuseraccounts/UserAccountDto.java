package com.example.autoluxe.payload.getuseraccounts;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.utils.BooleanDeserializer;
import com.example.autoluxe.utils.DateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAccountDto {

    private Integer id;

    private String name;
    @JsonProperty
    @JsonDeserialize(using = BooleanDeserializer.class)
    private boolean hide;

    private String AD_name;

    private String pass;

    private Integer status;

    @JsonProperty
    @JsonDeserialize(using = DateTimeDeserializer.class)
    private LocalDateTime RFC_expires;


}
