package com.example.autoluxe.payload.getuseraccounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GetUserAccountResponse {

    @JsonProperty
    private List<UserAccountDto> accounts;
}
