package com.example.helloworld.controller;

import com.example.helloworld.model.Position;
import com.example.helloworld.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/positions")
@CrossOrigin
public class PositionController {
    @Autowired
    private PositionService positionService;
    @GetMapping("{id}")
    public ResponseEntity<List<Position>> findPositionsByCompany_Id(@PathVariable int id) {
        return new ResponseEntity<>(positionService.findPositionsByCompany_Id(id), HttpStatus.OK);
    }
}
