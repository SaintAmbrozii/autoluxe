package com.example.autoluxe.configserver;

import com.example.autoluxe.domain.Role;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class DevConfig implements InitializingBean {

    private final UserService userService;


    private static final String adminMail = "admin@mail.ru";



    public User createUserAdmin() throws Exception {
        User user = new User();
        user.setActive(true);
        user.setEmail(adminMail);
        user.setName("Админ");
        user.setRole(Role.ROLE_ADMIN);
        user.setActive(true);
        user.setBalance(BigDecimal.valueOf(100000.00));

        return user;
    }

    public User createUser() throws Exception {
        User user = new User();
        user.setActive(true);
        user.setEmail("test@mail.ru");
        user.setName("Тест Юзер");
        user.setRole(Role.ROLE_USER);
        user.setActive(true);
        user.setBalance(BigDecimal.valueOf(100000.00));

        return user;
    }

    public User createUser2() throws Exception {
        User user = new User();
        user.setActive(true);
        user.setEmail("test2@mail.ru");
        user.setName("Тест Юзер 2");
        user.setRole(Role.ROLE_USER);
        user.setActive(true);
        user.setBalance(BigDecimal.valueOf(100000.00));

        return user;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        userService.createTestUser(createUserAdmin());
        userService.createTestUser(createUser());
        userService.createTestUser(createUser2());

    }


    @Profile({"dev"})
    @Component("DevServer")
    public static class HttpServer {
        @Bean
        public ServletWebServerFactory servletContainer() {
            Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
            connector.setPort(8080);
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
            tomcat.addAdditionalTomcatConnectors(connector);
            return tomcat;
        }
    }
}
