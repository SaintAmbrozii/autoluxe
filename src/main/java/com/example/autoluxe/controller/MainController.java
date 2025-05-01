package com.example.autoluxe.controller;

import com.example.autoluxe.dto.MessageDto;
import com.example.autoluxe.payload.auth.ApiResponse;
import com.example.autoluxe.payload.contactform.ContactDto;
import com.example.autoluxe.service.ContactFormService;
import com.example.autoluxe.service.MessageFormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MainController {

    private final ContactFormService contactFormService;
    private final MessageFormService messageFormService;

    public MainController(ContactFormService contactFormService, MessageFormService messageFormService) {
        this.contactFormService = contactFormService;
        this.messageFormService = messageFormService;
    }

    @PostMapping("contact")
    public ResponseEntity<ApiResponse> sendContact(@RequestBody @Valid ContactDto dto) {

        contactFormService.save(dto);

        return ResponseEntity.ok().body(new ApiResponse(true,"Send message susesfully!"));

    }



}
