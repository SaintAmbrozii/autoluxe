package com.example.autoluxe.service;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.domain.VerificationToken;
import com.example.autoluxe.repo.AccountRepo;
import com.example.autoluxe.repo.UserRepo;
import com.example.autoluxe.repo.VerificationTokenRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepo accountRepo;
    private final UserRepo userRepo;
    private final VerificationTokenRepo
            verificationTokenRepo;

    public AccountService(AccountRepo accountRepo, UserRepo userRepo, VerificationTokenRepo verificationTokenRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
        this.verificationTokenRepo = verificationTokenRepo;
    }

    public List<UserAccount> getAll () {
        return accountRepo.findAll();
    }

    public List<UserAccount> findAllByUserId(Long id) {
        return accountRepo.findAllByUserId(id);
    }

    public Optional<UserAccount> findByEpcId(Integer id){
        return accountRepo.findUserAccountByEpcId(id);
    }

    public User getUser(VerificationToken verificationToken) {
        return verificationTokenRepo.findByToken(verificationToken).getUser();
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = VerificationToken.builder()
                .user(user)
                .token(token).build();
        verificationTokenRepo.save(myToken);
    }

    public VerificationToken getVerificationToken(String VerificationToken) {
        return verificationTokenRepo.findByToken(VerificationToken);
    }




    public Optional<UserAccount> findById(Long id) {
        return accountRepo.findById(id);
    }


    public void accountSaveList(List<UserAccount> list) {
        accountRepo.saveAll(list);
    }
}
