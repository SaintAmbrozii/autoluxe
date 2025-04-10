package com.example.autoluxe.service;

import com.example.autoluxe.domain.Calculate;
import com.example.autoluxe.repo.CalcRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalculationService {

    private final CalcRepo calcRepo;

    public CalculationService(CalcRepo calcRepo) {
        this.calcRepo = calcRepo;
    }

    public List<Calculate> findAll() {
        return calcRepo.findAll();
    }

    public Calculate create(Calculate calculate) {
       return calcRepo.save(calculate);
    }

    public Optional<Calculate> calculate(List<Integer> params, Integer days) {


        return calcRepo.findCalculateByDaysAndCalculates(days,params);


    }
}
