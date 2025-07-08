package com.example.helloworld.repository;

import com.example.helloworld.model.Tkn;
import com.example.helloworld.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TypeRepository extends JpaRepository<Type, Integer> {
}
