package com.example.autoluxe.repo;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepo extends JpaRepository<VerificationToken,Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
