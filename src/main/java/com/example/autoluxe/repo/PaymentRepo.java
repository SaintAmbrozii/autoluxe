package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Payments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.time.ZonedDateTime;
import java.util.List;

public interface PaymentRepo extends JpaRepository<Payments,Long>, JpaSpecificationExecutor<Payments> {

    Page<Payments> findByManagerId(Long userId, Pageable pageable);

    Long countAllByManagerId(Long id);

    List<Payments> findAllByUserId(Long id);






}
