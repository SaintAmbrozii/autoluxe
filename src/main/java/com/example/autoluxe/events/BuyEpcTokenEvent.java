package com.example.autoluxe.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
@Getter
@Setter
public class BuyEpcTokenEvent extends ApplicationEvent {

    String Btoken;
    String usertoken;

    public BuyEpcTokenEvent(String Btoken, String usertoken) {
        super(Btoken);
        this.Btoken=Btoken;
        this.usertoken=usertoken;
    }
}
