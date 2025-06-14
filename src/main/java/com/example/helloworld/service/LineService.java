package com.example.helloworld.service;

import com.example.helloworld.model.Line;

import java.util.List;

public interface LineService {
    List<Line> findLinesBySite_Id(int id);
    void save(Line line);
    Line findById(int id);
    void delete(Line line);
}
