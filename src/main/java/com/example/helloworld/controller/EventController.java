package com.example.helloworld.controller;

import com.example.helloworld.model.Company;
import com.example.helloworld.model.Event;
import com.example.helloworld.service.CompanyService;
import com.example.helloworld.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/events")
@CrossOrigin
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public ResponseEntity<List<Event>> findAll() {
        List<Event> eventList = eventService.findAll();
        return new ResponseEntity<>(eventList, HttpStatus.OK);
    }
}
