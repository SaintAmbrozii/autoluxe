package com.example.autoluxe.controller;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.dto.PaymentDto;
import com.example.autoluxe.repo.PaymentRepo;
import com.example.autoluxe.service.PaymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;

    public PaymentController(PaymentService paymentService, PaymentRepo paymentRepo) {
        this.paymentService = paymentService;
        this.paymentRepo = paymentRepo;
    }

    @GetMapping("list")
    public Page<PaymentDto> list(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                 @RequestParam(value = "count", defaultValue = "100", required = false) int size,
                                 @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
                                 @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty) {
        Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
        Pageable pageable = PageRequest.of(page, size, sort);
        return paymentRepo.findAll(pageable).map(PaymentDto::toDto);
    }

 //   @GetMapping("list")
 //   public Page<PaymentDto> byAdmin(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
  //                               @RequestParam(value = "count", defaultValue = "100", required = false) int size,
 //                                @RequestParam(value = "order", defaultValue = "DESC", required = false) Sort.Direction direction,
  //                               @RequestParam(value = "sort", defaultValue = "id", required = false) String sortProperty) {
 //       Sort sort = Sort.by(new Sort.Order(direction, sortProperty));
  //      Pageable pageable = PageRequest.of(page, size, sort);
  //      return paymentService.findAllAdmin(pageable);
  //  }




//    @GetMapping("admin")
 //   public List<Payments> findByAdmin() {
 //       return paymentService.findAllAdmin();
 //   }

 //   public List<Payments> findAll() {
  //        return paymentService.findAll();
  //}

}
