package com.example.autoluxe.repo;

import com.example.autoluxe.domain.MessageForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageFormRepo extends JpaRepository<MessageForm,Long> {
}
