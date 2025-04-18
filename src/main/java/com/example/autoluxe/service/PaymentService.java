package com.example.autoluxe.service;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.PaymentDto;
import com.example.autoluxe.dto.searchcriteria.PaymentSearchCriteria;
import com.example.autoluxe.dto.specifications.PaymentSpecs;
import com.example.autoluxe.repo.PaymentRepo;
import com.example.autoluxe.utils.DateUtils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;

    public PaymentService(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }


    public Page<PaymentDto> findByUser(Long userId, Pageable pageable) {

        return paymentRepo.findByManagerId(userId, pageable)
                .map(PaymentDto::toDto);
    }

    public Page<PaymentDto> findByCriteria(User user, PaymentSearchCriteria criteria) {
        return paymentRepo
                .findAll(PaymentSpecs.accordingToReportProperties(user, criteria), criteria.getPageable())
                .map(PaymentDto::toDto);
    }

    public Page<PaymentDto> findByUsersCriteria(PaymentSearchCriteria criteria) {
        return paymentRepo
                .findAll(PaymentSpecs.accordingToCriteriaProperties(criteria), criteria.getPageable())
                .map(PaymentDto::toDto);
    }

    public Long countAllManagerId(Long id) {
        return paymentRepo.countAllByManagerId(id);
    }

    public Long countAllPayments() {
        return paymentRepo.count();
    }

    public Payments save(Payments payments) {
        return paymentRepo.save(payments);
    }


}