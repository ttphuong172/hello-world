package com.example.helloworld.controller;

import com.example.helloworld.model.Impact;
import com.example.helloworld.service.ImpactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/impacts")
@CrossOrigin
public class ImpactController {
    @Autowired
    private ImpactService impactService;

    @GetMapping("")
    public ResponseEntity<List<Impact>> findAll() {
        List<Impact> impactList = impactService.findAll();
        return new ResponseEntity<>(impactList, HttpStatus.OK);
    }
}
