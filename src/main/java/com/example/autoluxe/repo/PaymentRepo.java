package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payments,Long> {

    List<Payments> findAllByPayAdminIsTrue();
}
