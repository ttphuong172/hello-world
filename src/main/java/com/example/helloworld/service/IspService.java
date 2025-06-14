package com.example.helloworld.service;

import com.example.helloworld.model.Isp;

import java.util.List;

public interface IspService {
    List<Isp> findAll();
    Isp findById(int id);
    List<Isp> findByName(String name);
}
