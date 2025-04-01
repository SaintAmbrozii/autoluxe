package com.example.autoluxe.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.time.Clock;

@Getter
@Setter
public class GetUserAccountsEvent extends ApplicationEvent {

    private Long userId;


    public GetUserAccountsEvent(Long userId) {
        super(userId);
        this.userId=userId;
    }
}
