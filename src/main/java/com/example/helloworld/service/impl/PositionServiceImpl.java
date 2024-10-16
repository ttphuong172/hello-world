package com.example.helloworld.service.impl;

import com.example.helloworld.model.Position;
import com.example.helloworld.repository.PositionRepository;
import com.example.helloworld.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionRepository positionRepository;
    @Override
    public List<Position> findPositionsByCompany_Id(int id) {
        return positionRepository.findPositionsByCompany_Id(id);
    }
}
