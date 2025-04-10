package com.example.autoluxe.controller;

import com.example.autoluxe.domain.Calculate;
import com.example.autoluxe.payload.getbuytoken.BuyTokenRequest;
import com.example.autoluxe.service.CalculationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/calculation")
public class CalcucationController {

    private final CalculationService calculationService;

    public CalcucationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping
    public List<Calculate> findAll() {
        return calculationService.findAll();
    }

    @PatchMapping
    public Optional<Calculate> get(@RequestBody BuyTokenRequest request) {
        return calculationService.calculate(request.getParam(),request.getDays());
    }

    @PostMapping
    public Calculate create(@RequestBody Calculate calculate) {
        return calculationService.create(calculate);
    }
}
