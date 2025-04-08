package com.example.autoluxe.service;

import com.example.autoluxe.domain.Calculate;
import com.example.autoluxe.repo.CalcRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Optional<Calculate> calculate(Set<Integer> params, Integer days) {

        List<Calculate> list = calcRepo.findAllByDays(days);


      Optional<Calculate> calculate = list.stream()
              .filter(cacl->cacl.getParams().contains(params))
              .findFirst();

       return calculate;


    }
}
