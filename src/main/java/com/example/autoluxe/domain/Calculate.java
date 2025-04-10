package com.example.autoluxe.domain;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "calculations")
public class Calculate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @Type(ListArrayType.class)
    @Column(name = "params",columnDefinition = "integer[]")
    private List<Integer> params;

    @Column(name = "days")
    private Integer days;

    @Column(name = "price")
    private Double price;

    @OneToMany
    private List<ParamsCalculate> calculates;
}
