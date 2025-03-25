package com.example.autoluxe.service;



import com.example.autoluxe.domain.Token;
import com.example.autoluxe.domain.User;
import com.example.autoluxe.repo.TokenRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserTokenService {

    private final TokenRepo tokenRepo;

    public UserTokenService(TokenRepo tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    public Optional<Token> findByToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public void saveToken(User user, String jwt){
        Token token = Token.builder()
                .token(jwt)
                .user(user)
                .build();

        tokenRepo.save(token);
    }

    public void revokeAllUserToken(User user){
        List<Token> allValidTokenByUser
                = tokenRepo.findAllValidTokenByUser(user.getId());

        if(allValidTokenByUser.isEmpty()) return;

        allValidTokenByUser.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepo.saveAll(allValidTokenByUser);
    }

    public void deleteToken(Long id) {
        tokenRepo.deleteById(id);
    }
}
