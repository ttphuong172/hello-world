package com.example.helloworld.controller;


import com.example.helloworld.model.Type;

import com.example.helloworld.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/types")
@CrossOrigin
public class TypeController {
    @Autowired
    private TypeService typeService;

    @GetMapping("")
    public ResponseEntity<List<Type>> findAll() {
        List<Type> typeList = typeService.findAll();
        return new ResponseEntity<>(typeList, HttpStatus.OK);
    }
}
