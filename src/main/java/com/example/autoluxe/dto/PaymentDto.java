package com.example.autoluxe.dto;

import com.example.autoluxe.domain.Payments;
import com.example.autoluxe.utils.DateUtils;
import com.example.autoluxe.utils.MoneyUtils;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
public class PaymentDto {

    private Long id;

    private Long userId;

    private Long managerId;

    private String userEmail;

    private String timestamp;

    private Double summa;

    private String payment;

    public static PaymentDto toDto(Payments payments) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payments.getId());
        paymentDto.setManagerId(payments.getManagerId());
        paymentDto.setTimestamp(DateUtils.ddmmyyyy_hhmmssZ(payments.getTimestamp()));
        paymentDto.setSumma(payments.getSumma().doubleValue());
        paymentDto.setUserId(payments.getUserId());
        paymentDto.setUserEmail(payments.getUserEmail());
        paymentDto.setPayment(MoneyUtils.formatRU(payments.getSumma().doubleValue()));
        return paymentDto;
    }
}
