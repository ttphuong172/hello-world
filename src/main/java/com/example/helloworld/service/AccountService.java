package com.example.helloworld.service;


import com.example.helloworld.model.Account;

import java.util.List;

public interface AccountService {
    Account findById(String username);
    void save(Account account);
    List<Account> findAll();
}
