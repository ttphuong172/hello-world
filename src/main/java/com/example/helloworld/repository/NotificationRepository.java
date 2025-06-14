package com.example.helloworld.repository;

import com.example.helloworld.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findAllByOrderByIdDesc();
}
