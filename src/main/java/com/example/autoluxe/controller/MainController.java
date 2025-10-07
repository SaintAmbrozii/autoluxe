package com.example.autoluxe.controller;

import com.example.autoluxe.events.ContactFormEvent;
import com.example.autoluxe.events.ContactFormEventListener;
import com.example.autoluxe.payload.auth.ApiResponse;
import com.example.autoluxe.payload.contactform.ContactDto;
import com.example.autoluxe.service.ContactFormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MainController {



    @GetMapping
    public String message() {
        return String.valueOf("доступна главная страница");
    }

    @GetMapping("registrationSuccess")
    public String registreationSucess() {
        return String.valueOf("регистрация пройденна");
    }





}
