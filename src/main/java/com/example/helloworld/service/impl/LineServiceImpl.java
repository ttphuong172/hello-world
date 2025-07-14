package com.example.helloworld.service.impl;

import com.example.helloworld.model.Line;
import com.example.helloworld.repository.LineRepository;
import com.example.helloworld.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LineServiceImpl implements LineService {
    @Autowired
    private LineRepository lineRepository;
    @Override
    public List<Line> findLinesBySite_Id(int id) {
        return lineRepository.findLinesBySite_IdOrderBySequence(id);
    }

    @Override
    public void save(Line line) {
        lineRepository.save(line);
    }

    @Override
    public Line findById(int id) {
        return lineRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Line line) {
        lineRepository.delete(line);
    }

}
