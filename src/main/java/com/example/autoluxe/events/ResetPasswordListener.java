package com.example.autoluxe.events;


import com.example.autoluxe.domain.MailType;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ResetPasswordListener implements ApplicationListener<ResetPasswordEvent> {

    @Value("${BASIC_URL}")
    private String basicUrl;

    private final MailService mailService;

    public ResetPasswordListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(ResetPasswordEvent event) {

    }

    private void confirmRegistration(ResetPasswordEvent event) {
        User user = event.getUser();
        String token = event.getUrl();


        String confirmationUrl
                = basicUrl + "/api/auth/changePassword?token=" + token;
        try {
            mailService.sendChangePassword(MailType.CHANGE_PASSWORD,user,confirmationUrl);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
