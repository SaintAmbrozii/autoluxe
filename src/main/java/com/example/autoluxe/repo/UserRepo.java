package com.example.autoluxe.repo;

import com.example.autoluxe.domain.Token;
import com.example.autoluxe.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findUserByEmail(String email);




}
