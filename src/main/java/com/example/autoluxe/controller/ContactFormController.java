package com.example.autoluxe.controller;

import com.example.autoluxe.domain.ContactForm;
import com.example.autoluxe.service.ContactFormService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/contact")
public class ContactFormController {


    private final ContactFormService contactFormService;

    public ContactFormController(ContactFormService contactFormService) {
        this.contactFormService = contactFormService;
    }

    public List<ContactForm> findAll() {
        return contactFormService.findAll();
    }
}
