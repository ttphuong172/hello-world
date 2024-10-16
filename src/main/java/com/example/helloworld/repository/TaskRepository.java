package com.example.helloworld.repository;

import com.example.helloworld.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByOrderByIsVisibleDescCompanyAscPositionAscIdAsc();
    List<Task> findTaskByPositionIdAndIsVisibleIsTrue(int id);
    @Query("SELECT t FROM Task t WHERE (t.name like %:name%) and (:companyId is null or t.company.id=:companyId) order by t.isVisible desc, t.company.id asc , t.position.id asc, t.id asc ")
    List<Task> searchTask(@Param("name") String name, @Param("companyId") Optional<Integer> companyId);
}
