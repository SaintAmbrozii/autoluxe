package com.example.autoluxe.controller;

import com.example.autoluxe.events.ContactFormEvent;
import com.example.autoluxe.events.ContactFormEventListener;
import com.example.autoluxe.payload.auth.ApiResponse;
import com.example.autoluxe.payload.contactform.ContactDto;
import com.example.autoluxe.service.ContactFormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class MainController {

    private final ContactFormService contactFormService;
    private final ContactFormEventListener contactFormEventListener;

    public MainController(ContactFormService contactFormService,
                          ContactFormEventListener contactFormEventListener) {
        this.contactFormService = contactFormService;
        this.contactFormEventListener = contactFormEventListener;
    }

    @GetMapping
    public String message() {
        return String.valueOf("доступна главная страница");
    }

    @PostMapping("contact")
    public ResponseEntity<ApiResponse> sendContact(@RequestBody @Valid ContactDto dto) {

        contactFormService.save(dto);
        contactFormEventListener.onApplicationEvent(new ContactFormEvent(dto.getName(), dto.getPhone()));

        return ResponseEntity.ok().body(new ApiResponse(true,"Send message susesfully!"));

    }



}
