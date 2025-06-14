package com.example.helloworld.controller;

import com.example.helloworld.model.TknType;
import com.example.helloworld.model.Type;
import com.example.helloworld.service.TknTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/tkntype")
@CrossOrigin
public class TknTypeController {
    @Autowired
    private TknTypeService tknTypeService;

    @GetMapping("")
    public ResponseEntity<List<TknType>> findAll() {
        List<TknType> tknTypeList = tknTypeService.findAll();
        return new ResponseEntity<>(tknTypeList, HttpStatus.OK);
    }
}
