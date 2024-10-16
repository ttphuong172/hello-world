package com.example.helloworld.service.impl;

import com.example.helloworld.model.Type;
import com.example.helloworld.repository.TypeRepository;
import com.example.helloworld.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;
    @Override
    public List<Type> findAll() {
        return typeRepository.findAll();
    }
}
