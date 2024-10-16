package com.example.helloworld.repository;

import com.example.helloworld.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    List<Position> findPositionsByCompany_Id(int id);
}
