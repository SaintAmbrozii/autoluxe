package com.example.autoluxe.dto;

import com.example.autoluxe.domain.UserAccount;
import lombok.Data;

@Data
public class UserAccountDto {

    private Long id;

    private Integer epcId;

    private String login;

    private String name;

    private String pass;

    private String RFCExpires;

    public static UserAccountDto toDto (UserAccount userAccount) {
        UserAccountDto accountDto = new UserAccountDto();
        accountDto.setId(userAccount.getId());
        accountDto.setRFCExpires(userAccount.getRFCExpires());
        accountDto.setPass(userAccount.getPass());
        accountDto.setLogin(userAccount.getLogin());
        accountDto.setEpcId(userAccount.getEpcId());
        return accountDto;
    }

}
