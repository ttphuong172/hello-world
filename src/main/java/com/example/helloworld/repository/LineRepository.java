package com.example.helloworld.repository;

import com.example.helloworld.model.Line;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LineRepository extends JpaRepository<Line, Integer> {
    List<Line> findLinesBySite_Id(int id);
}
