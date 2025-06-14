package com.example.helloworld.controller;

import com.example.helloworld.model.Notification;
import com.example.helloworld.model.Ping;
import com.example.helloworld.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/notification")
@CrossOrigin
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("")
    public ResponseEntity<List<Notification>> findAll() {
        List<Notification> notificationList = notificationService.findAll();
        return new ResponseEntity<>(notificationList, HttpStatus.OK);
    }
}
