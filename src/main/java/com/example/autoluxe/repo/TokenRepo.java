package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token,Long> {

    @Query("""
            SELECT t FROM Token t WHERE t.user.id =:id
            AND (t.expired = false OR t.revoked = false)
            """
    )
    List<Token> findAllValidTokenByUser(Long id);

    @Query("SELECT t FROM Token t WHERE t.token=:token")
    Optional<Token> findByToken(String token);
}
