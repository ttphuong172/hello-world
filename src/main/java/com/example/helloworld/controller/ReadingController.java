package com.example.helloworld.controller;

import com.example.helloworld.model.Reading;
import com.example.helloworld.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/reading")
@CrossOrigin
public class ReadingController {
    @Autowired
    private ReadingService readingService;
    @PostMapping("")
    public ResponseEntity<Reading> save(@RequestBody Reading reading) {
        reading.setReadingDate(LocalDateTime.now());
        readingService.save(reading);
        return new ResponseEntity<>(reading, HttpStatus.OK);
    }
}
