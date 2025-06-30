package com.example.helloworld.controller;

import com.example.helloworld.model.Country;
import com.example.helloworld.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/countries")
@CrossOrigin
public class CountryController {
    @Autowired
    private CountryService countryService;
    @GetMapping("")
    public ResponseEntity<List<Country>> findAll() {
        return new ResponseEntity<>(countryService.findAll(),HttpStatus.OK);
    }
}
