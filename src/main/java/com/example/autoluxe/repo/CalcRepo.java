package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Calculate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CalcRepo extends JpaRepository<Calculate,Long> {

    List<Calculate> findAllByDays(Integer days);



}
