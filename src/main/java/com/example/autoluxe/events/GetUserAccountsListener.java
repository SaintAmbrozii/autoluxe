package com.example.autoluxe.events;

import com.example.autoluxe.service.ApiService;
import com.example.autoluxe.service.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class GetUserAccountsListener implements ApplicationListener<GetUserAccountsEvent> {

    private final ApiService apiService;

    public GetUserAccountsListener(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void onApplicationEvent(GetUserAccountsEvent event) {
        this.addUserAccounts(event);
    }

    private void addUserAccounts(GetUserAccountsEvent event) {
        Long userId = event.getUserId();
        apiService.getUserAccount(userId);
    }
}
