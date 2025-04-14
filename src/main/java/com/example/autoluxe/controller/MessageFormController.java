package com.example.autoluxe.controller;

import com.example.autoluxe.domain.MessageForm;
import com.example.autoluxe.service.MessageFormService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/messages")
public class MessageFormController {

    private final MessageFormService messageFormService;

    public MessageFormController(MessageFormService messageFormService) {
        this.messageFormService = messageFormService;
    }

    @GetMapping
    public List<MessageForm> getAll(){
        return messageFormService.getAll();
    }
}
