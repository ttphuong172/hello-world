package com.example.helloworld.service;

import com.example.helloworld.model.Monitor;

import java.util.List;

public interface MonitorService {
    List<Monitor> findAll();
    void save(Monitor monitor);
    Monitor findById (int id);
    void delete(Monitor monitor);
}
