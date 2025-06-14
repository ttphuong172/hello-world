package com.example.helloworld.controller;


import com.example.helloworld.config.JwtUtil;
import com.example.helloworld.config.MyUserDetailsService;
import com.example.helloworld.model.dto.JwtRequest;
import com.example.helloworld.model.dto.JwtResponse;
import com.example.helloworld.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AccountService accountService;

    @PostMapping("/signin")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception {
        String token = null;
        UserDetails userDetails;
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
        } catch (DisabledException e) {
            return new JwtResponse(token);
        } catch (BadCredentialsException e) {
            return new JwtResponse(token);
        }
        userDetails = myUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
        token = jwtUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
