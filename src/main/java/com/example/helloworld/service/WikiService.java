package com.example.helloworld.service;

import com.example.helloworld.model.Wiki;

import java.util.List;

public interface WikiService {
    List<Wiki> findAll();
    Wiki findById(int id);
    List<Wiki> searchByKeyword(String keyword);
}
