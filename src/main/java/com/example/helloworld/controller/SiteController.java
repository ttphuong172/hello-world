package com.example.helloworld.controller;

import com.example.helloworld.model.Site;
import com.example.helloworld.model.dto.SiteDTO;
import com.example.helloworld.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/sites")
@CrossOrigin
public class SiteController {
    @Autowired
    private SiteService siteService;

    @GetMapping("")
    public ResponseEntity<List<Site>> findAllByOrderByName() {
        List<Site> siteList = siteService.findAllByOrderByName();
        return new ResponseEntity<>(siteList, HttpStatus.OK);
    }

    @GetMapping("dto")
    public ResponseEntity<List<SiteDTO>> findAllDTOByOrderByCompanyAscNameAsc() {
        List<Site> siteList = siteService.findAllByOrderByName();
//        For SiteDTO
        List<SiteDTO> siteDTOList = new ArrayList<>();
        for (int i = 0; i < siteList.size(); i++) {
            SiteDTO siteDTO = new SiteDTO();
            siteDTO.setId(siteList.get(i).getId());
            siteDTO.setName(siteList.get(i).getName());
            siteDTO.setNation(siteList.get(i).getNation());
            siteDTO.setCity(siteList.get(i).getCity());
            siteDTO.setAddress(siteList.get(i).getAddress());
            siteDTO.setZoneId(siteList.get(i).getZoneId());
            siteDTO.setConfigure(siteList.get(i).getConfigure());
            siteDTO.setConfigureKr(siteList.get(i).getConfigureKr());
            siteDTO.setCompany(siteList.get(i).getCompany());
            siteDTO.setRic(siteList.get(i).getRic());
            siteDTO.setLineList(siteList.get(i).getLineList());
            siteDTO.setContactList(siteList.get(i).getContactList());

            siteDTOList.add(siteDTO);
        }


        return new ResponseEntity<>(siteDTOList, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<SiteDTO> findById(@PathVariable int id) {
        Site site = siteService.findById(id);

        SiteDTO siteDTO = new SiteDTO();
        siteDTO.setId(site.getId());
        siteDTO.setName(site.getName());
        siteDTO.setNation(site.getNation());
        siteDTO.setCity(site.getCity());
        siteDTO.setAddress(site.getAddress());
        siteDTO.setZoneId(site.getZoneId());
        siteDTO.setConfigure(site.getConfigure());
        siteDTO.setConfigureKr(site.getConfigureKr());
        siteDTO.setCompany(site.getCompany());
        siteDTO.setRic(site.getRic());
        siteDTO.setLineList(site.getLineList());
        siteDTO.setContactList(site.getContactList());

        return new ResponseEntity<>(siteDTO, HttpStatus.OK);
    }

    @GetMapping("company/{id}")
    public ResponseEntity<List<SiteDTO>> findSitesByCompany_IdOrderByName(@PathVariable int id) {
        List<Site> siteList = siteService.findSitesByCompany_IdOrderByRicAscNameAsc(id);
//        For SiteDTO
        List<SiteDTO> siteDTOList = new ArrayList<>();
        for (int i = 0; i < siteList.size(); i++) {
            SiteDTO siteDTO = new SiteDTO();
            siteDTO.setId(siteList.get(i).getId());
            siteDTO.setName(siteList.get(i).getName());
            siteDTO.setNation(siteList.get(i).getNation());
            siteDTO.setCity(siteList.get(i).getCity());
            siteDTO.setAddress(siteList.get(i).getAddress());
            siteDTO.setZoneId(siteList.get(i).getZoneId());
            siteDTO.setConfigure(siteList.get(i).getConfigure());
            siteDTO.setConfigureKr(siteList.get(i).getConfigureKr());
            siteDTO.setCompany(siteList.get(i).getCompany());
            siteDTO.setRic(siteList.get(i).getRic());
            siteDTO.setLineList(siteList.get(i).getLineList());
            siteDTO.setContactList(siteList.get(i).getContactList());

            siteDTOList.add(siteDTO);
        }
        return new ResponseEntity<>(siteDTOList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Site> save(@RequestBody Site site) {
        siteService.save(site);
        return new ResponseEntity<>(site, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Site site) {
        Site siteCurrent = siteService.findById(id);
        if (siteCurrent == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        siteCurrent.setName(site.getName());
        siteCurrent.setNation(site.getNation());
        siteCurrent.setCity(site.getCity());
        siteCurrent.setAddress(site.getAddress());
        siteCurrent.setZoneId(site.getZoneId());
        siteCurrent.setConfigure(site.getConfigure());
        siteCurrent.setConfigureKr(site.getConfigureKr());
        siteCurrent.setCompany(site.getCompany());
        siteCurrent.setRic(site.getRic());

        siteService.save(siteCurrent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<List<SiteDTO>> searchSite(@RequestParam(required = false) String name, @RequestParam Optional<Integer> companyId) {
        List<Site> siteList = siteService.searchSite(name, companyId);

//        For SiteDTO
        List<SiteDTO> siteDTOList = new ArrayList<>();
        for (int i = 0; i < siteList.size(); i++) {
            SiteDTO siteDTO = new SiteDTO();
            siteDTO.setId(siteList.get(i).getId());
            siteDTO.setName(siteList.get(i).getName());
            siteDTO.setNation(siteList.get(i).getNation());
            siteDTO.setCity(siteList.get(i).getCity());
            siteDTO.setAddress(siteList.get(i).getAddress());
            siteDTO.setZoneId(siteList.get(i).getZoneId());
            siteDTO.setConfigure(siteList.get(i).getConfigure());
            siteDTO.setConfigureKr(siteList.get(i).getConfigureKr());
            siteDTO.setCompany(siteList.get(i).getCompany());
            siteDTO.setRic(siteList.get(i).getRic());
            siteDTO.setLineList(siteList.get(i).getLineList());
            siteDTO.setContactList(siteList.get(i).getContactList());

            siteDTOList.add(siteDTO);
        }

        return new ResponseEntity<>(siteDTOList, HttpStatus.OK);
    }
}
