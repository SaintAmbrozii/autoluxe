package com.example.autoluxe.service;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.PaymentDto;
import com.example.autoluxe.dto.searchcriteria.PaymentSearchCriteria;
import com.example.autoluxe.dto.specifications.PaymentSpecs;
import com.example.autoluxe.repo.PaymentRepo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final ReportService reportService;

    public PaymentService(PaymentRepo paymentRepo, ReportService reportService) {
        this.paymentRepo = paymentRepo;
        this.reportService = reportService;
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

    public List<Payments> findPaymentsByCriteria(User user, PaymentSearchCriteria criteria) {
        return paymentRepo
                .findAll(PaymentSpecs.accordingToReportProperties(user, criteria), criteria.getPageable())
                .getContent();
    }

    public byte[] createExportCSVFile(User user, PaymentSearchCriteria searchCriteria) throws Exception {
        log.debug(">>> createExportCSVFile for {}", user);
        return reportService.composeCSV(findPaymentsByCriteria(user, searchCriteria),user.getActive());
    }

    public byte[] createExportXLSFile(User user, PaymentSearchCriteria searchCriteria) throws Exception {
        log.debug(">>> createExportXLSFile for {}", user);
        return reportService.composeXLS(findPaymentsByCriteria(user, searchCriteria),user.getActive());
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