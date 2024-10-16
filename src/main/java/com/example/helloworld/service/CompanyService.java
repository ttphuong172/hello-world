package com.example.helloworld.service;

import com.example.helloworld.model.Company;


import java.util.List;

public interface CompanyService {
    List<Company> findAll();
    Company findById(int id);
}
