package com.example.helloworld.service.impl;



import com.example.helloworld.model.Account;
import com.example.helloworld.repository.AccountRepository;
import com.example.helloworld.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Account findById(String username) {
//        return accountRepository.findById(username).orElseThrow(()->new UsernameNotFoundException("Not found"));
        return accountRepository.findById(username).orElse(null);
    }

    @Override
    public void save(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }
}
