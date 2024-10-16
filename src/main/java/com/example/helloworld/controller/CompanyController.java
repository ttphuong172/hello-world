package com.example.helloworld.controller;

import com.example.helloworld.model.Company;
import com.example.helloworld.model.Site;
import com.example.helloworld.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/companys")
@CrossOrigin
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping("")
    public ResponseEntity<List<Company>> findAll() {
        List<Company> companyList = companyService.findAll();
        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Company> findById(@PathVariable int id) {
        return new ResponseEntity<>(companyService.findById(id), HttpStatus.OK);
    }
}
