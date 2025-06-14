package com.example.helloworld.service.impl;

import com.example.helloworld.model.TknType;
import com.example.helloworld.repository.TknTypeRepository;
import com.example.helloworld.service.TknTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TknTypeServiceImpl implements TknTypeService {
    @Autowired
    private TknTypeRepository tknTypeRepository;
    @Override
    public List<TknType> findAll() {
        return tknTypeRepository.findAll();
    }
}
