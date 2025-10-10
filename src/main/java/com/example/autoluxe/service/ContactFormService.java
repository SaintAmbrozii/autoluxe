package com.example.autoluxe.service;

import com.example.autoluxe.domain.ContactForm;
import com.example.autoluxe.payload.contactform.ContactDto;
import com.example.autoluxe.repo.ContactFormRepo;
import com.example.autoluxe.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactFormService {

    private final ContactFormRepo contactFormRepo;

    public ContactFormService(ContactFormRepo contactFormRepo) {
        this.contactFormRepo = contactFormRepo;
    }

    public List<ContactForm> findAll() {
        return contactFormRepo.findAll();
    }

    public ContactForm save(ContactDto dto) {
        ContactForm form = new ContactForm();
        form.setName(dto.getName());
        form.setPhone(dto.getPhone());
        form.setDateTime(DateUtils.now());
       return contactFormRepo.save(form);
    }
}
