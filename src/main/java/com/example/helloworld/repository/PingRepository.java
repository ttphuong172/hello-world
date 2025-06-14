package com.example.helloworld.repository;

import com.example.helloworld.model.Ping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PingRepository extends JpaRepository<Ping, Integer> {
    List<Ping> findAllByOrderByIdDesc();
}
