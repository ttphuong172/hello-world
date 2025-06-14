package com.example.helloworld.repository;

import com.example.helloworld.model.Monitor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitorRepository extends JpaRepository<Monitor, Integer> {
    List<Monitor> findAllByOrderByIsUp();
}
