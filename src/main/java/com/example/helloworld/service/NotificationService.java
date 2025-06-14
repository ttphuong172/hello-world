package com.example.helloworld.service;

import com.example.helloworld.model.Notification;

import java.util.List;

public interface NotificationService {
    void save(Notification notification);
    List<Notification> findAll();
}
