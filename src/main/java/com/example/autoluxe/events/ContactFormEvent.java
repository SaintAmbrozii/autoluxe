package com.example.autoluxe.events;

import org.springframework.context.ApplicationEvent;

public class ContactFormEvent extends ApplicationEvent {

    String name;

    String phone;

    public ContactFormEvent(String name,String phone) {
        super(name);
        this.name = name;
        this.phone = phone;
    }
}
