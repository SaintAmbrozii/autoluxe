package com.example.autoluxe.service;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.repo.PaymentRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;

    public PaymentService(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public List<Payments> findAll() {
        return paymentRepo.findAll();
    }

    public List<Payments> findAllAdmin() {
        return paymentRepo.findAllByPayAdminIsTrue();
    }

    public Payments save(Payments payments) {
        return paymentRepo.save(payments);
    }
}
