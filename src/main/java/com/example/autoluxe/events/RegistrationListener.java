package com.example.autoluxe.events;

import com.example.autoluxe.domain.MailType;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.service.AccountService;
import com.example.autoluxe.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final AccountService accountService;
    private final MessageSource messageSource;
    private final MailService mailService;
    private final JavaMailSender mailSender;

    private static final String basicUrl = "http://localhost:8080/api/auth";

    private static final String TEMPLATE_NAME = "registration";
    private static final String MAIL_SUBJECT = "Registration Confirmation";

    public RegistrationListener(AccountService accountService,
                                MessageSource messageSource, MailService mailService, JavaMailSender mailSender) {
        this.accountService = accountService;

        this.messageSource = messageSource;
        this.mailService = mailService;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        confirmRegistration(event);


    }

    private void confirmRegistration(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(user, token);

        String confirmationUrl
                = basicUrl + "/regitrationConfirm?token=" + token;

        try {
            mailService.sendTemplateEmail(MailType.REGISTRATION,user,confirmationUrl);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
