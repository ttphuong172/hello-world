package com.example.helloworld.repository;

import com.example.helloworld.model.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadingRepository extends JpaRepository<Reading, Integer> {
}
