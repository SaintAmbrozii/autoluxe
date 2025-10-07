package com.example.autoluxe.controller;

import com.example.autoluxe.domain.ContactForm;
import com.example.autoluxe.events.ContactFormEvent;
import com.example.autoluxe.events.ContactFormEventListener;
import com.example.autoluxe.payload.auth.ApiResponse;
import com.example.autoluxe.payload.contactform.ContactDto;
import com.example.autoluxe.service.ContactFormService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/contact")
public class ContactFormController {


    private final ContactFormService contactFormService;
    private final ContactFormEventListener contactFormEventListener;

    public ContactFormController(ContactFormService contactFormService, ContactFormEventListener contactFormEventListener) {
        this.contactFormService = contactFormService;
        this.contactFormEventListener = contactFormEventListener;
    }

    @PostMapping("add")
    public ResponseEntity<ApiResponse> sendContact(@RequestBody @Valid ContactDto dto) {

        contactFormService.save(dto);
        contactFormEventListener.onApplicationEvent(new ContactFormEvent(dto.getName(), dto.getPhone()));

        return ResponseEntity.ok().body(new ApiResponse(true,"Send message susesfully!"));

    }

    @GetMapping
    public List<ContactForm> findAll() {
        return contactFormService.findAll();
    }
}
