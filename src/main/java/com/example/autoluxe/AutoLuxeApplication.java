package com.example.autoluxe;

import com.example.autoluxe.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {AppProperties.class})
@SpringBootApplication
public class AutoLuxeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoLuxeApplication.class, args);
    }

}
