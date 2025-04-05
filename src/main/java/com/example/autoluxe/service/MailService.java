package com.example.autoluxe.service;

import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
@Slf4j
public class MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    public static final String NEW_USER_ACCOUNT_VERIFICATION = "new user account verification";

    public MailService(Configuration configuration,
                       JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.configuration = configuration;
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendHtmlEmail(String name, String to, String token) {
        try{
            Context context = new Context();
            context.setVariable("name" , name);
         //   context.setVariable("url" , getVerificationUrl(host,token));
        //    context.setVariables(Map.of("name" , name  ,"url" , getVerificationUrl(host,token)) );
            String text = templateEngine.process("emailTemplate" , context);
       //     MimeMessage message = getMimeMessage();
        //    MimeMessageHelper helper = new MimeMessageHelper(message , true ,"UTF-8");
      //      helper.setPriority(1);
      //      helper.setSubject(NEW_USER_ACCOUNT_VERIFICATION);
       //     helper.setFrom(fromEmail);
      //      helper.setTo(to);
      //      helper.setText(text,true);
        //    mailSender.send(message);


        }catch (Exception exception){
            System.out.println(exception.getMessage());
            throw  new RuntimeException(exception.getMessage());
        }
    }




    //   public void sendEmail(User user, MailType mailType, Properties params) {
    //       switch (mailType){
    //           case REGISTRATION -> sendRegistrationEmail(user,params);
//            case ZAYAVKA -> sendBookingToEmail(user,params);
///            case ZVONOK -> sendCancelBookingToEmail(user,params);
//            default -> {}
    //       }
    //   }



        //   @SneakyThrows
        //   private void sendRegistrationEmail(User user,Properties params) {
        //       MimeMessage mimeMessage = mailSender.createMimeMessage();
        //       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,false,"UTF-8");
        //       helper.setSubject("Спасибо за регистрацию в нашем сервисе" + user.getName());
//        helper.setTo(user.getEmail());
//        String content = getRegistrationEmailContent(user,params);
//        helper.setText(content,true);


        //       mailSender.send(mimeMessage);
        //       log.info("письмо регистрации отправлено получателю");
        //   }


//    @SneakyThrows
        //  private void sendBookingToEmail(User user,Properties params) {
        //     MimeMessage mimeMessage = mailSender.createMimeMessage();
        ////      MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,false,"UTF-8");
        //      helper.setSubject("Вы произвели бронирование билетов на рейс" + user.getName());
        //      helper.setTo(user.getEmail());
        //       String content = getBookingEmailContent(user,params);
        //       helper.setText(content,true);
        //       mailSender.send(mimeMessage);
        //       log.info("письмо покупки билетов отправлено получателю");

        //   }
        //   @SneakyThrows
        //   private void sendCancelBookingToEmail(User user,Properties params) {
        //       MimeMessage mimeMessage = mailSender.createMimeMessage();
        //       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,false,"UTF-8");
        //     helper.setSubject("Произошел возврат билетов на рейс" + user.getName());
        //       helper.setTo(user.getEmail());
        //     String content = getCancelEmailContent(user,params);

        //     helper.setText(content,true);
        //      mailSender.send(mimeMessage);
        //      log.info("письмо отмены бронирования отправлено получателю");
        //  }



}
