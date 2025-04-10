package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Calculate;
import com.example.autoluxe.domain.ParamsCalculate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculateParamRepo extends JpaRepository<ParamsCalculate,Long> {


}
