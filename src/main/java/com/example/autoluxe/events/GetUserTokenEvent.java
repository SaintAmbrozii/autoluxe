package com.example.autoluxe.events;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class GetUserTokenEvent extends ApplicationEvent {

    private Long userId;

    public GetUserTokenEvent(Long userId) {
        super(userId);
        this.userId=userId;
    }
}
