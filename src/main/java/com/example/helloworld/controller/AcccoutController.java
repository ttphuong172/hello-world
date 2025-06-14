package com.example.helloworld.controller;

import com.example.helloworld.model.Account;
import com.example.helloworld.model.dto.PasswordDTO;
import com.example.helloworld.service.AccountService;
//import com.example.helloworld.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("api/accounts")
@CrossOrigin
@EnableAsync
public class AcccoutController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("")
    public ResponseEntity<List<Account>> findAll() {
        return new ResponseEntity<>(accountService.findAll(), HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<Account> findById(@PathVariable String username) {
//        System.out.println(username);
        Account account = accountService.findById(username);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Account> save(@RequestBody Account account) {
        Account accountCurrent = accountService.findById(account.getUsername());
        if (accountCurrent != null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String asciiLowerCase = asciiUpperCase.toLowerCase();
        String digits = "1234567890";
        String asciiChars = asciiUpperCase + asciiLowerCase + digits;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(asciiChars.charAt(random.nextInt(asciiChars.length())));
        }
        account.setPassword(passwordEncoder.encode(password));
        account.setEnable(true);
        accountService.save(account);

//        String contentEmail = "Your Account username:" + account.getUsername() + ", password:" + password;
//        try {
//            emailSenderService.sendEmail(account.getEmail(), contentEmail);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @PutMapping("{username}")
    public ResponseEntity<String> update(@PathVariable String username, @RequestBody Account account) {
        Account accountCurrent = accountService.findById(username);
        if (accountCurrent == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        accountCurrent.setFullname(account.getFullname());
        accountCurrent.setEmail(account.getEmail());
        accountCurrent.setRole(account.getRole());
//        System.out.println(account.isEnable());
        accountCurrent.setEnable(account.isEnable());
        accountService.save(accountCurrent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("reset/{username}")
    public ResponseEntity<String> resetPass(@PathVariable String username) {
        Account account = accountService.findById(username);
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        String asciiUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String asciiLowerCase = asciiUpperCase.toLowerCase();
        String digits = "1234567890";
        String asciiChars = asciiUpperCase + asciiLowerCase + digits;
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            password.append(asciiChars.charAt(random.nextInt(asciiChars.length())));
        }
        
        account.setPassword(passwordEncoder.encode(password));
        accountService.save(account);
//        String contentEmail = "Your Account username:" + account.getUsername() + ", password:" + password;
//        try {
//            emailSenderService.sendEmail(account.getEmail(), contentEmail);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordDTO passwordDTO) throws Exception {
        Account account = accountService.findById(passwordDTO.getUsername());
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), account.getPassword())) {
            throw new Exception("old password is incorrect");
        }
        if (!passwordDTO.getNewPassword().equals(passwordDTO.getComfirmNewPassword())) {
            throw new Exception("new password no match");
        }
        account.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        accountService.save(account);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
