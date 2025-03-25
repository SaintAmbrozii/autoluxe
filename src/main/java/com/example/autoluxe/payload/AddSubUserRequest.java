package com.example.autoluxe.payload;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddSubUserRequest {

    private String token;
}
