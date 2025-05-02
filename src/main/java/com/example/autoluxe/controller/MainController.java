package com.example.autoluxe.controller;

import com.example.autoluxe.events.ContactFormEvent;
import com.example.autoluxe.events.ContactFormEventListener;
import com.example.autoluxe.payload.auth.ApiResponse;
import com.example.autoluxe.payload.contactform.ContactDto;
import com.example.autoluxe.service.ContactFormService;
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
    private final ContactFormEventListener contactFormEventListener;

    public MainController(ContactFormService contactFormService, ContactFormEventListener contactFormEventListener) {
        this.contactFormService = contactFormService;
        this.contactFormEventListener = contactFormEventListener;
    }

    @PostMapping("contact")
    public ResponseEntity<ApiResponse> sendContact(@RequestBody @Valid ContactDto dto) {

        contactFormService.save(dto);
        contactFormEventListener.onApplicationEvent(new ContactFormEvent(dto.getName(), dto.getPhone()));

        return ResponseEntity.ok().body(new ApiResponse(true,"Send message susesfully!"));

    }



}
