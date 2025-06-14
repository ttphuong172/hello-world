package com.example.helloworld.controller;


import com.example.helloworld.model.Monitor;
import com.example.helloworld.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/monitor")
@CrossOrigin
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    @GetMapping("")
    public ResponseEntity<List<Monitor>> findAll() {
        List<Monitor> monitorList = monitorService.findAll();
        return new ResponseEntity<>(monitorList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Monitor> save(@RequestBody Monitor monitor) {
        monitor.setIsUp(Boolean.TRUE);
        monitor.setIsAlarm(Boolean.TRUE);
        monitorService.save(monitor);
        return new ResponseEntity<>(monitor, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Monitor monitor = monitorService.findById(id);
        monitorService.delete(monitor);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Monitor> findById(@PathVariable int id) {
        return new ResponseEntity<>(monitorService.findById(id), HttpStatus.OK);
    }

    @GetMapping("isalarm/{id}")
    public ResponseEntity<String> isAlarm(@PathVariable int id){
        Monitor monitor = monitorService.findById(id);
        monitor.setIsAlarm(!monitor.getIsAlarm());
        monitorService.save(monitor);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
