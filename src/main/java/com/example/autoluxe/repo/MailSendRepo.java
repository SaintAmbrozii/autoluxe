package com.example.autoluxe.repo;

import com.example.autoluxe.domain.MailSenderForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailSendRepo extends JpaRepository<MailSenderForm,Long> {
}
