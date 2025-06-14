package com.example.helloworld.service.impl;

import com.example.helloworld.model.Reading;
import com.example.helloworld.repository.ReadingRepository;
import com.example.helloworld.service.ReadingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReadingServiceImpl implements ReadingService {
    @Autowired
    private ReadingRepository readingRepository;
    @Override
    public void save(Reading reading) {
        readingRepository.save(reading);
    }
}
