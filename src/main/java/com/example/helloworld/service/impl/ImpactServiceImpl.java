package com.example.helloworld.service.impl;

import com.example.helloworld.model.Impact;
import com.example.helloworld.repository.ImpactRepository;
import com.example.helloworld.service.ImpactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImpactServiceImpl implements ImpactService {
    @Autowired
    private ImpactRepository impactRepository;
    @Override
    public List<Impact> findAll() {
        return impactRepository.findAll();
    }
}
