package com.example.autoluxe.payload.contactform;

import com.example.autoluxe.utils.validator.ContactNumberConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContactDto {

    @NotBlank
    @Size(min = 2,max = 20)
    private String name;
    @NotBlank
    @Size(min = 8,max = 14)
    @ContactNumberConstraint
    private String phone;
}
