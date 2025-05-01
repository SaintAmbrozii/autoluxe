package com.example.autoluxe.events;

import com.example.autoluxe.domain.MailType;
import com.example.autoluxe.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ContactFormEventListener implements ApplicationListener<ContactFormEvent> {


    private final MailService mailService;

    public ContactFormEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(ContactFormEvent event) {

        String username = event.name;
        String phone = event.phone;

        try {
            mailService.sendContactForm(MailType.ZVONOK,username,phone);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
