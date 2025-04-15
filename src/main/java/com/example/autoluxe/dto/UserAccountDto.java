package com.example.autoluxe.dto;

import com.example.autoluxe.domain.UserAccount;
import lombok.Data;

@Data
public class UserAccountDto {

    private Integer epcId;

    private String login;

    private String name;

    private String pass;

    private String expires;

    public static UserAccountDto toDto (UserAccount userAccount) {
        UserAccountDto accountDto = new UserAccountDto();
        accountDto.setExpires(userAccount.getRFCExpires());
        accountDto.setName(userAccount.getName());
        accountDto.setPass(userAccount.getPass());
        accountDto.setLogin(userAccount.getLogin());
        accountDto.setEpcId(userAccount.getEpcId());
        return accountDto;
    }

}
