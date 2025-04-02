package com.example.autoluxe.events;

import com.example.autoluxe.domain.MailType;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.service.AccountService;
import com.example.autoluxe.service.MailService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Properties;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final AccountService accountService;
    private final MessageSource messageSource;
    private final MailService mailService;

    public RegistrationListener(AccountService accountService,
                                MessageSource messageSource, MailService mailService) {
        this.accountService = accountService;

        this.messageSource = messageSource;
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {


    }

    private void confirmRegistration(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        accountService.createVerificationToken(user, token);

        String confirmationUrl
                = event.getAppUrl() + "/regitrationConfirm?token=" + token;
        String message = messageSource.getMessage("message.regSucc", null, event.getLocale());

        Properties props = new Properties();
        props.setProperty("token", token);
        props.setProperty("confirm_url",message + "\r\n" + confirmationUrl);

        mailService.sendEmail(user,MailType.REGISTRATION,new Properties());
    }
}
