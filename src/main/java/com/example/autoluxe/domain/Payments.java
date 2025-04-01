package com.example.autoluxe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
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
    @Column(name = "created")
    private LocalDateTime created;
    @Column(name = "summa")
    private Double summa;

    private boolean payAdmin;
}
