package com.example.helloworld.repository;

import com.example.helloworld.model.Isp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IspRepository extends JpaRepository<Isp, Integer> {
    List<Isp> findByName(String name);
}
