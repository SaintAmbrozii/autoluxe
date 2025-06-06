package com.example.autoluxe.utils.validator;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContactNumberValidator implements ConstraintValidator<ContactNumberConstraint,String> {


    @Override
    public void initialize(ContactNumberConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String contactField, ConstraintValidatorContext constraintValidatorContext) {
        return contactField != null && contactField.matches("(^8|7|\\+7)((\\d{10})|(\\s\\(\\d{3}\\)\\s\\d{3}\\s\\d{2}\\s\\d{2}))")
                && (contactField.length() > 8) && (contactField.length() < 14);
    }
}
