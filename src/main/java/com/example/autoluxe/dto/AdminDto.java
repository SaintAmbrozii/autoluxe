package com.example.autoluxe.dto;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminDto {

    private Long id;

    private String name;

    private String phone;

    private String email;


    public static AdminDto toDto(User user) {
        AdminDto dto = new AdminDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        return dto;
    }
}
