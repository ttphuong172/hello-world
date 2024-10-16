package com.example.helloworld.service.impl;

import com.example.helloworld.model.Isp;
import com.example.helloworld.repository.IspRepository;
import com.example.helloworld.service.IspService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IspServiceImpl implements IspService {
    @Autowired
    private IspRepository ispRepository;
    @Override
    public List<Isp> findAll() {
        return ispRepository.findAll();
    }

    @Override
    public Isp findById(int id) {
        return ispRepository.findById(id).orElse(null);
    }

    @Override
    public List<Isp> findByName(String name) {
        return ispRepository.findByName(name);
    }
}
