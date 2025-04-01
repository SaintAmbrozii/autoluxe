package com.example.autoluxe.controller;

import com.example.autoluxe.service.AccountService;

public class AccountController {

    private final AccountService accountService;


    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
}
