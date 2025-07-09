package com.example.helloworld.repository;

import com.example.helloworld.model.Tkn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TknRepository extends JpaRepository<Tkn, Integer> {

    List<Tkn> findAllByIsVisibleIsTrueOrderByIdDesc();

    @Query("SELECT t FROM Tkn t WHERE " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.content) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.tknType.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.summaryContent) LIKE LOWER(CONCAT('%', :keyword, '%')) ) AND (t.isVisible = true ) AND (:tknTypeId is null or t.tknType.id=:tknTypeId) AND (:companyId is null or t.company.id=:companyId) AND (:siteId is null or t.site.id=:siteId) ORDER BY t.id DESC ")
    List<Tkn> searchByKeyword(@Param("keyword") String keyword, @Param("tknTypeId") Optional<Integer> tknTypeId,@Param("companyId") Optional<Integer> companyId, @Param("siteId") Optional<Integer> siteId);


}
