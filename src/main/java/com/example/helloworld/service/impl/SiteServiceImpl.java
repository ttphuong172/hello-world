package com.example.helloworld.service.impl;

import com.example.helloworld.model.Site;
import com.example.helloworld.repository.SiteRepository;
import com.example.helloworld.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteServiceImpl implements SiteService {
    @Autowired
    private SiteRepository siteRepository;
    @Override
    public List<Site> findAllByOrderByName() {
        return siteRepository.findAllByOrderByCompanyAscNameAsc();
    }

    @Override
    public List<Site> findSitesByCompany_IdOrderByRicAscNameAsc(int id) {
        return siteRepository.findSitesByCompany_IdOrderByRicAscNameAsc(id);
    }

    @Override
    public void save(Site site) {
        siteRepository.save(site);
    }

    @Override
    public Site findById(int id) {
        return siteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Site> searchSite(String name, Optional<Integer> companyId) {
        return siteRepository.searchSite(name, companyId);
    }
}
