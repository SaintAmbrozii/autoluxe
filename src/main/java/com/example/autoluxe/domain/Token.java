package com.example.autoluxe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "token")
@ToString
@Builder
public class Token {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    private boolean expired = false;

    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @Column(name = "duration")
    private Date duration;





}
