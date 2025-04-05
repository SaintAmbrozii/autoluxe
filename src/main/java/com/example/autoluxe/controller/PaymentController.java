package com.example.autoluxe.controller;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.service.PaymentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public List<Payments> findByAdmin() {
        return paymentService.findAllAdmin();
    }

    public List<Payments> findAll() {
        return paymentService.findAll();
    }
}
