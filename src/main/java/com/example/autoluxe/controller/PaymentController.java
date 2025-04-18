package com.example.autoluxe.controller;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.dto.PaymentDto;
import com.example.autoluxe.dto.PaymentPage;
import com.example.autoluxe.dto.searchcriteria.PaymentSearchCriteria;
import com.example.autoluxe.repo.PaymentRepo;
import com.example.autoluxe.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;

    public PaymentController(PaymentService paymentService, PaymentRepo paymentRepo) {
        this.paymentService = paymentService;
        this.paymentRepo = paymentRepo;
    }

    @GetMapping() //станица платежей всех юзеров
    public PaymentPage list(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                 @RequestParam(value = "count", defaultValue = "100", required = false) int size,
                                 @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                                 @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty) {
        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);

        return PaymentPage.of(paymentRepo.findAll(pageable).map(PaymentDto::toDto));
    }

    @PostMapping("filter") //поиск по платежам всех юзеров
    public PaymentPage filter(@RequestBody PaymentSearchCriteria searchCriteria) {
        log.debug("PaymentSearchCriteria={}", searchCriteria);
        searchCriteria.validate();
        return PaymentPage.of(paymentService.findByUsersCriteria(searchCriteria));
    }

    @GetMapping("countall") //счетчик платежи всех юзеров
    public Long countAll(){
        return paymentService.countAllPayments();
    }



    @GetMapping("countallmanager") //счетчик платежей админа
    public Long countAllManager(@AuthenticationPrincipal User user){
        return paymentRepo.countAllByManagerId(user.getId());
    }

    @GetMapping("list") //страница платежей админа
    public PaymentPage payments(@AuthenticationPrincipal User user,
                                @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                @RequestParam(value = "count", defaultValue = "100", required = false) int size,
                                @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                                @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty) {
        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);
        return PaymentPage.of(paymentService.findByUser(user.getId(), pageable));
    }

    @PostMapping("list/filter") //поиск по платежам админа
    public PaymentPage adminFilter(@AuthenticationPrincipal User user, @RequestBody PaymentSearchCriteria searchCriteria) {
        log.debug("PaymentSearchCriteria={}", searchCriteria);
        searchCriteria.validate();
        return PaymentPage.of(paymentService.findByCriteria(user, searchCriteria));
    }






//    @GetMapping("admin")
 //   public List<Payments> findByAdmin() {
 //       return paymentService.findAllAdmin();
 //   }

 //   public List<Payments> findAll() {
  //        return paymentService.findAll();
  //}

}
