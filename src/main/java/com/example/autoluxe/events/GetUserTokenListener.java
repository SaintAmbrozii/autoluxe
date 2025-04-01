package com.example.autoluxe.events;

import com.example.autoluxe.service.ApiService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class GetUserTokenListener implements ApplicationListener<GetUserTokenEvent> {

    private final ApiService apiService;

    public GetUserTokenListener(ApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public void onApplicationEvent(GetUserTokenEvent event) {
        Long userId = event.getUserId();
        apiService.getUserToken(userId);
    }
}
