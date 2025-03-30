package com.example.autoluxe.payload.confirmbuy;

import com.example.autoluxe.payload.confirmbuy.ConfirmByAccountDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ConfirmByResponse {

    @JsonProperty
    private List<ConfirmByAccountDto> byAccountDtoList;
}
