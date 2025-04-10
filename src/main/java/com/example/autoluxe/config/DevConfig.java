package com.example.autoluxe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Profile("dev")
@Component
@RequiredArgsConstructor
public class DevConfig {



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
