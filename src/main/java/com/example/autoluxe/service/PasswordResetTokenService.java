package com.example.autoluxe.service;

import com.example.autoluxe.domain.PasswordResetToken;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.repo.PasswordResetRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;

@Service
public class PasswordResetTokenService {

    private final PasswordResetRepo passwordResetRepo;

    public PasswordResetTokenService(PasswordResetRepo passwordResetRepo) {
        this.passwordResetRepo = passwordResetRepo;
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setToken(token);
        myToken.setUser(user);
        myToken.setExpiryDates(24);
        passwordResetRepo.save(myToken);
    }

    public PasswordResetToken findByToken (String token) {
        return passwordResetRepo.findByToken(token).orElseThrow();
    }

    public void delete(PasswordResetToken token) {
        passwordResetRepo.delete(token);
    }



    public String validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passwordResetToken = passwordResetRepo.findByToken(token);

        PasswordResetToken passToken = passwordResetToken.get();
        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }
}
