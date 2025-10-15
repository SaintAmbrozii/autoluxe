package com.example.autoluxe.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public enum Type {

    ADD_BALANCE("Пополнение баланса пользователя"),
    FULL("Полный доступ"),
    PASS_CARS("Легковые"),
    TRUKS("Грузовые"),
    TIS("TIS"),
    AUTODATA("AutoData"),
    TECHDOC("TechDoc"),
    TECHDATA("TechData"),
    FULL_AUTODATA("Полный доступ + AutoData"),
    FULL_TECHDOC("Полный доступ + TechDoc"),
    FULL_TECHDATA("Полный доступ + TechData"),
    FULL_AUTO_TECHDOC("Полный доступ + AutoData + TechDoc"),
    FULL_AUTO_TECHDATA("Полный доступ + AutoData + TechData"),
    FULL_AUTO_TECHDOCDATA("Полный доступ + TechDoc + TechData"),
    FULL_AUTO_TECHDOC_DATA("Полный доступ + AutoData + TechDoc + TechData"),
    AUTODATA_TECHDOC("AutoData + TechDoc"),
    AUTODATA_TECHDATA("AutoData + TechData"),
    TECHDOC_DATA("TechDoc + TechData"),
    AUTODATA_TECHDOC_DATA("AutoData + TechDoc + TechData");

    Type(String name) {
    }
}
