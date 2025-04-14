package com.example.autoluxe.service;

import com.example.autoluxe.domain.MessageForm;
import com.example.autoluxe.dto.MessageDto;
import com.example.autoluxe.repo.MessageFormRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageFormService {

    private final MessageFormRepo messageFormRepo;

    public MessageFormService(MessageFormRepo messageFormRepo) {
        this.messageFormRepo = messageFormRepo;
    }

    public List<MessageForm> getAll(){
        return messageFormRepo.findAll();
    }

    public void save (MessageDto dto) {
        MessageForm messageForm = new MessageForm();
        messageForm.setMessage(dto.getMessage());
        messageForm.setName(dto.getName());
        messageForm.setEmail(dto.getEmail());
        messageForm.setPhone(dto.getPhone());
        messageFormRepo.save(messageForm);
    }
}
