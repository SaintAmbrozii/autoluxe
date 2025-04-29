package com.example.autoluxe.payload.contactform;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import com.example.autoluxe.utils.validator.NullOrNotBlank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactDto {

    @NotBlank(message = "Имя не должно быть пустым.")
    private String name;
    @NotBlank(message = "Телефон не должно быть пустым.")
    @ContactNumberConstraint
    private String phone;
}
