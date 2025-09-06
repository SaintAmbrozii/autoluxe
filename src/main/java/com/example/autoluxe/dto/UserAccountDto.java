package com.example.autoluxe.dto;

import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.utils.DateUtils;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserAccountDto {

    private Long id;

    private Integer epcId;

    private String login;

    private String name;

    private String pass;

    private LocalDateTime expires;

    public static UserAccountDto toDto (UserAccount userAccount) {
        UserAccountDto accountDto = new UserAccountDto();
        accountDto.setId(userAccount.getId());
        accountDto.setExpires(userAccount.getRFCExpires());
        accountDto.setName(userAccount.getName());
        accountDto.setPass(userAccount.getPass());
        accountDto.setLogin(userAccount.getLogin());
        accountDto.setEpcId(userAccount.getEpcId());
        return accountDto;
    }

}
