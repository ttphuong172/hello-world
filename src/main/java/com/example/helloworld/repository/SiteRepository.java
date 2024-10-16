package com.example.helloworld.repository;

import com.example.helloworld.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Integer> {
    List<Site> findAllByOrderByCompanyAscNameAsc();

    List<Site> findSitesByCompany_IdOrderByRicAscNameAsc(int id);

    @Query("SELECT s FROM Site s WHERE (s.name like %:name%) and (:companyId is null or s.company.id=:companyId) order by s.company.id asc , s.name asc")
    List<Site> searchSite(@Param("name") String name, @Param("companyId") Optional<Integer> companyId);
}
