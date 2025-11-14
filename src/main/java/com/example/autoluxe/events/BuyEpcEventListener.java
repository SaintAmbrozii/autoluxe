package com.example.autoluxe.events;

import com.example.autoluxe.domain.MailType;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.service.MailService;
import jakarta.mail.MessagingException;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BuyEpcEventListener implements ApplicationListener<ByAccountEvent> {

    private final MailService mailService;

    public BuyEpcEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public void onApplicationEvent(ByAccountEvent event) {

        User user = event.getUser();
        String password = event.getPassword();
        String login = event.getLogin();
        String uri = "https://online.epcinfo.ru/login/index.php#login";

        try {
            mailService.sendEPCAccount(MailType.BUY_EPC,user,password,login,uri);
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
}
