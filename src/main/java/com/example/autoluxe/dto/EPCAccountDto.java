package com.example.autoluxe.dto;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
public class EPCAccountDto {

    private Long id;

    private Integer epcId;

    private String login;

    private boolean hide;

    private String ADName;

    private String pass;

    private Integer status;

    private String RFCExpires;






}
