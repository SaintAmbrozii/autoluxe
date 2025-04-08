package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Payments;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface PaymentRepo extends JpaRepository<Payments,Long> {


}
