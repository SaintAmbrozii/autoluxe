package com.example.autoluxe.controller;

import com.example.autoluxe.domain.Calculate;
import com.example.autoluxe.service.CalculationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/calculcation")
public class CalcucationController {

    private final CalculationService calculationService;

    public CalcucationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @GetMapping
    public List<Calculate> findAll() {
        return calculationService.findAll();
    }

    @PostMapping
    public Calculate create(@RequestBody Calculate calculate) {
        return calculationService.create(calculate);
    }
}
