package com.example.autoluxe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments",indexes = {@Index(name = "payment_timestamp_index", columnList = "timestamp")})
@ToString
public class Payments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userId")
    private Long userId;
    @Column(name = "managerId")
    private Long managerId;
    @Column(name = "userEmail")
    private String userEmail;
    @Column(name = "timestamp")
    private ZonedDateTime timestamp;
    @Column(name = "summa",columnDefinition = "NUMERIC(10,2)")
    private BigDecimal summa;
    @Column(name = "type")
    private String type;

}
