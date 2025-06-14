package com.example.helloworld.service.impl;

import com.example.helloworld.model.Monitor;
import com.example.helloworld.repository.MonitorRepository;
import com.example.helloworld.service.MonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonitorServiceImpl implements MonitorService {
    @Autowired
    private MonitorRepository monitorRepository;
    @Override
    public List<Monitor> findAll() {
        return monitorRepository.findAllByOrderByIsUp();
    }

    @Override
    public void save(Monitor monitor) {
        monitorRepository.save(monitor);
    }

    @Override
    public Monitor findById(int id) {
        return monitorRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Monitor monitor) {
        monitorRepository.delete(monitor);
    }
}
