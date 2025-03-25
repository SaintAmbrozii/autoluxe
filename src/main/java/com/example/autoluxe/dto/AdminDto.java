package com.example.autoluxe.dto;

import com.example.autoluxe.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdminDto {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String epic_token;

    private String partner_token;

    private List<String> epics_ids;

    private String epics_name;

    private String epics_password;

    private String epics_login;

    public static AdminDto toDto(User user) {
        AdminDto dto = new AdminDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setEpic_token(user.getEpic_token());
        dto.setPartner_token(user.getPartner_token());
        dto.setPassword(user.getPassword());
        dto.setEpics_ids(user.getEpics_ids());
        dto.setEpics_name(user.getUsername());
        dto.setEpics_login(user.getEpics_login());
        dto.setEpics_password(user.getEpics_password());
        return dto;
    }
}
