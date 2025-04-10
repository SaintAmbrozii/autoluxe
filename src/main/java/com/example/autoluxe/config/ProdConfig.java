package com.example.autoluxe.config;

import lombok.RequiredArgsConstructor;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({"prod"})
@Component("ProductionServer")
@RequiredArgsConstructor
public class ProdConfig {

    @Profile({"prod"})
    @Component("ProductionServer")
    public static class HttpServer {
        @Bean
        public TomcatServletWebServerFactory servletContainer() {
            TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {

                @Override
                protected void postProcessContext(Context context) {
                    SecurityConstraint securityConstraint = new SecurityConstraint();
                    securityConstraint.setUserConstraint("CONFIDENTIAL");
                    SecurityCollection collection = new SecurityCollection();
                    collection.addPattern("/*");
                    securityConstraint.addCollection(collection);
                    context.addConstraint(securityConstraint);
                }
            };

            tomcat.addAdditionalTomcatConnectors(redirectConnector());
            return tomcat;
        }

        private Connector redirectConnector() {
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setScheme("http");
            connector.setPort(8080);
            connector.setSecure(false);
            connector.setRedirectPort(443);

            return connector;
        }


    }

}
