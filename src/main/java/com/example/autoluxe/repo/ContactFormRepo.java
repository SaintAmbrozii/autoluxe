package com.example.autoluxe.repo;

import com.example.autoluxe.domain.ContactForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactFormRepo extends JpaRepository<ContactForm,Long> {
}
