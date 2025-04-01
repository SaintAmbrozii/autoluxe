package com.example.autoluxe.dto;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String token;


    
    public static UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setToken(user.getEpic_token());
        dto.setPassword(user.getPassword());
        return dto;
    }
}
