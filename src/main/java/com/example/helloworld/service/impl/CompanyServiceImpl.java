package com.example.helloworld.service.impl;

import com.example.helloworld.model.Company;
import com.example.helloworld.repository.CompanyRepository;
import com.example.helloworld.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyRepository companyRepository;
    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Company findById(int id) {
        return companyRepository.findById(id).orElse(null);
    }
}
