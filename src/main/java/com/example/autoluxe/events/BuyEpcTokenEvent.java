package com.example.autoluxe.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class BuyEpcTokenEvent extends ApplicationEvent {

    String Btoken;
    String user_token;
    public BuyEpcTokenEvent(String Btoken, String user_token) {
        super(Btoken);
        this.Btoken=Btoken;
        this.user_token=user_token;
    }
}
