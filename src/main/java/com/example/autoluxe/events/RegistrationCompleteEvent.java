package com.example.autoluxe.events;

import com.example.autoluxe.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {


    private User user;

    public RegistrationCompleteEvent(User user) {
        super(user);
        this.user=user;

    }
}
