package com.example.autoluxe.service;

import com.example.autoluxe.repo.ContactFormRepo;
import org.springframework.stereotype.Service;

@Service
public class ContactFormService {

    private final ContactFormRepo contactFormRepo;

    public ContactFormService(ContactFormRepo contactFormRepo) {
        this.contactFormRepo = contactFormRepo;
    }
}
