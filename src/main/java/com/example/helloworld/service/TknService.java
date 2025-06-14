package com.example.helloworld.service;

import com.example.helloworld.model.Ping;
import com.example.helloworld.model.Tkn;
import com.example.helloworld.model.Wiki;

import java.util.List;

public interface TknService {
    List<Tkn> findAll();
    void save(Tkn tkn);
    Tkn findById(int id);
    List<Tkn> searchByKeyword(String keyword);
}
