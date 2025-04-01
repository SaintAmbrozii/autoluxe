package com.example.autoluxe.repo;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.payload.BuyAccountResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepo extends JpaRepository<UserAccount,Long> {

   Optional<UserAccount> findUserAccountByEpcId(Integer id);

   List<UserAccount> findAllByUserId(Long id);







}
