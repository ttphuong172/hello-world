package com.example.helloworld.service;

import com.example.helloworld.model.Site;
import java.util.List;
import java.util.Optional;

public interface SiteService {
    List<Site> findAllByOrderByName();
    List<Site> findSitesByCompany_IdOrderByRicAscNameAsc(int id);
    void save(Site site);
    Site findById(int id);
    List<Site> searchSite(String name, Optional<Integer> companyId);
}
