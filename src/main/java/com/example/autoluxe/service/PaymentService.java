package com.example.autoluxe.service;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.dto.PaymentDto;
import com.example.autoluxe.repo.PaymentRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;

    public PaymentService(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public List<Payments> findAll(Pageable pageable) {
        return paymentRepo.findAll();
    }



    public Payments save(Payments payments) {
        return paymentRepo.save(payments);
    }
}
