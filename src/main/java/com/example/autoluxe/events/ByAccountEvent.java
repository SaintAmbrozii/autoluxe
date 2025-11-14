package com.example.autoluxe.events;

import com.example.autoluxe.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ByAccountEvent extends ApplicationEvent {

    private User user;

    private String password;

    private String login;



    public ByAccountEvent(User user, String password, String login) {
        super(user);
        this.user=user;
        this.password=password;
        this.login=login;
    }
}
