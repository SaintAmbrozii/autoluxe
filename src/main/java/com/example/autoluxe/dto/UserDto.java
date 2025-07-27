package com.example.autoluxe.dto;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.utils.MoneyUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String balance;

    private String phone;


    
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setBalance(MoneyUtils.formatRU(user.getBalance().doubleValue()));
        return dto;
    }
}
