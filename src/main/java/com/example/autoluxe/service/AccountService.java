package com.example.autoluxe.service;

import com.example.autoluxe.domain.User;
import com.example.autoluxe.domain.UserAccount;
import com.example.autoluxe.repo.AccountRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepo accountRepo;

    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }

    public List<UserAccount> getAll () {
        return accountRepo.findAll();
    }

    public List<UserAccount> findAllByUserId(Long id) {
        return accountRepo.findAllByUserId(id);
    }

    public Optional<UserAccount> findByEpcId(Integer id){
        UserAccount account = new UserAccount();
        return accountRepo.findUserAccountByEpcId(id);
    }


    public Optional<UserAccount> findById(Long id) {
        return accountRepo.findById(id);
    }

    public UserAccount save(UserAccount account) {
      return  accountRepo.save(account);
    }

    public void accountSaveList(List<UserAccount> list) {
        accountRepo.saveAll(list);
    }
}
