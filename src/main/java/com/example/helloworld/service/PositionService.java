package com.example.helloworld.service;

import com.example.helloworld.model.Position;

import java.util.List;

public interface PositionService {
    List<Position> findPositionsByCompany_Id(int id);
}
