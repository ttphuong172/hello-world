package com.example.helloworld.repository;

import com.example.helloworld.model.Wiki;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WikiRepository extends JpaRepository<Wiki, Integer> {
    @Query("SELECT w FROM Wiki w WHERE LOWER(w.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(w.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Wiki> searchByKeyword(@Param("keyword") String keyword);
}
