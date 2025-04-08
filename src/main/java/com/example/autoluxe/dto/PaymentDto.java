package com.example.autoluxe.dto;

import com.example.autoluxe.domain.Payments;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentDto {

    private Long id;

    private Long userId;

    private Long managerId;

    private String userEmail;

    private LocalDateTime created;

    private BigDecimal summa;

    public static PaymentDto toDto(Payments payments) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setCreated(payments.getCreated());
        paymentDto.setSumma(payments.getSumma());
        paymentDto.setUserId(paymentDto.getUserId());
        paymentDto.setUserEmail(paymentDto.getUserEmail());
        return paymentDto;
    }
}
