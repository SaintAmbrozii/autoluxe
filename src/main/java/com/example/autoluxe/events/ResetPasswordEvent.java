package com.example.autoluxe.events;

import com.example.autoluxe.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ResetPasswordEvent extends ApplicationEvent {

    private String url;
    private User user;

    public ResetPasswordEvent(User user,String url) {
        super(user);
        this.user=user;
        this.url=url;
    }
}
