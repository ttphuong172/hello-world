package com.example.helloworld.controller;

import com.example.helloworld.model.Isp;
import com.example.helloworld.model.Line;
import com.example.helloworld.model.Site;
import com.example.helloworld.service.IspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/isps")
@CrossOrigin
public class IspController {
    @Autowired
    private IspService ispService;

    @GetMapping("")
    public ResponseEntity<List<Isp>> findAllByOrderByName() {
        List<Isp> ispList = ispService.findAll();
        return new ResponseEntity<>(ispList, HttpStatus.OK);
    }


    @GetMapping("{name}")
    public ResponseEntity<Isp> findByName(@PathVariable String name) {
        List<Isp> ispList = ispService.findByName(name);
        if (ispList.size()> 0){
            return new ResponseEntity<>(ispList.get(0), HttpStatus.OK);
        }
        return null;

    }
}
