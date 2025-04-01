package com.example.autoluxe.domain;

import com.example.autoluxe.payload.getuseraccounts.UserAccountDto;
import com.example.autoluxe.utils.BooleanDeserializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
@ToString
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "epcId")
    private Integer epcId;

    @Column(name = "name")
    private String name;

    @Column(name = "login")
    private String login;

    private boolean hide;

    @Column(name = "adName")
    private String ADName;

    @Column(name = "pass")
    private String pass;

    @Column(name = "status")
    private Integer status;

    @Column(name = "RFSExpires")
    private String RFCExpires;




}
