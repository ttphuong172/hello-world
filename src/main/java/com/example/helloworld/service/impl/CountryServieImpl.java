package com.example.helloworld.service.impl;

import com.example.helloworld.model.Country;
import com.example.helloworld.repository.CountryRepository;
import com.example.helloworld.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServieImpl implements CountryService {
    @Autowired
    private CountryRepository countryRepository;

    @Override
    public List<Country> findAll() {
        return countryRepository.findAll();
    }
}
