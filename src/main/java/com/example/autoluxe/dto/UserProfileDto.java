package com.example.autoluxe.dto;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.utils.MoneyUtils;
import lombok.Data;

@Data
public class UserProfileDto {

    private Long id;

    private String name;

    private String email;

    private String balance;

    private String phone;

    private String password;


    public static UserProfileDto toDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setPassword(user.getPassword());
        dto.balance = MoneyUtils.formatRU(user.getBalance().doubleValue());
        return dto;
    }
}
