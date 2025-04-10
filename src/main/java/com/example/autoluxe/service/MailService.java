package com.example.autoluxe.service;

import com.example.autoluxe.config.MailConfig;
import com.example.autoluxe.config.MailProperties;
import com.example.autoluxe.domain.MailType;
import com.example.autoluxe.domain.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@Slf4j
public class MailService {


    private static final String TEMPLATE_NAME = "emailTemplate";
    private static final String SPRING_LOGO_IMAGE = "templates/images/spring.png";
    private static final String PNG_MIME = "image/png";
    private static final String MAIL_SUBJECT = "Registration Confirmation";
    private static final String MAIL_FROM = "autoluxe@mail.ru";

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendTemplateEmail(MailType type, User user, String link) throws MessagingException {

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, false, "UTF-8");

        email.setTo(user.getEmail());
        email.setSubject(MAIL_SUBJECT);
        email.setFrom(MAIL_FROM);

        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("email", user.getEmail());
        ctx.setVariable("name", user.getName());
        ctx.setVariable("url", link);

        final String htmlContent = this.templateEngine.process(TEMPLATE_NAME, ctx);

        email.setText(htmlContent, true);


        mailSender.send(mimeMessage);
    }


    public void send(final String to, final String subject, final String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
         //   helper.setFrom(emailProperties.getSender().getEmail());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(email, true);mailSender.send(mimeMessage);

        } catch (MessagingException e){

            throw new IllegalStateException("Failed to send email");
        }
    }






}
