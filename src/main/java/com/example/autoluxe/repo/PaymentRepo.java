package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Payments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.time.ZonedDateTime;
import java.util.List;

public interface PaymentRepo extends JpaRepository<Payments,Long> {

    Page<Payments> findByManagerId(Long userId, Pageable pageable);

    Long countAllByManagerId(Long id);






}
