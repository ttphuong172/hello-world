package com.example.helloworld.service.impl;

import com.example.helloworld.model.Notification;
import com.example.helloworld.repository.NotificationRepository;
import com.example.helloworld.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    @Override
    public void save(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAllByOrderByIdDesc();
    }
}
