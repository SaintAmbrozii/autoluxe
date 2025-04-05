package com.example.autoluxe.events;

import com.example.autoluxe.service.ApiService;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class BuyEpcTokenEventListener implements ApplicationListener<BuyEpcTokenEvent> {

    private final ApiService apiService;

    public BuyEpcTokenEventListener(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void onApplicationEvent(BuyEpcTokenEvent event) {

        String Btoken = event.getBtoken();
        String userToken = event.getUsertoken();
        apiService.confirmBuy(Btoken,userToken);
    }
}
