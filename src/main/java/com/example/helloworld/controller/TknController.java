package com.example.helloworld.controller;

import com.example.helloworld.model.*;
import com.example.helloworld.model.dto.TknDTO;
import com.example.helloworld.model.dto.TknDTOReading;
import com.example.helloworld.service.TknService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tkn")
@CrossOrigin
public class TknController {
    @Autowired
    private TknService tknService;

    @GetMapping()
    public ResponseEntity<List<Tkn>> findAll() {
        return new ResponseEntity<>(tknService.findAll(), HttpStatus.OK);
    }

    @GetMapping("findall/{username}")
    public ResponseEntity<List<TknDTOReading>> findAll(@PathVariable String username) {

        List<TknDTOReading> tknDTOReadingList= new ArrayList<>();

        List<Tkn> tknList = tknService.findAll();
        for (int i = 0; i < tknList.size(); i ++){
            boolean isRead = false;
            TknDTOReading tknDTOReading = new TknDTOReading();

            tknDTOReading.setId(tknList.get(i).getId());
            tknDTOReading.setTitle(tknList.get(i).getTitle());
            tknDTOReading.setSummaryContent(tknList.get(i).getSummaryContent());
            tknDTOReading.setContent(tknList.get(i).getContent());
            tknDTOReading.setCreator(tknList.get(i).getCreator());
            tknDTOReading.setCreatingDate(tknList.get(i).getCreatingDate());
            tknDTOReading.setTknType(tknList.get(i).getTknType());
            tknDTOReading.setCompany(tknList.get(i).getCompany());
            tknDTOReading.setSite(tknList.get(i).getSite());
            tknDTOReading.setReadingList(tknList.get(i).getReadingList());
            tknDTOReading.setCommentList(tknList.get(i).getCommentList());

            for (int j = 0; j < tknList.get(i).getReadingList().size(); j ++){
                if (tknList.get(i).getReadingList().get(j).getAccount().getUsername().equals(username)) {
                    isRead = true;
                    break;
                };
            }
            tknDTOReading.setRead(isRead);

            tknDTOReadingList.add(tknDTOReading);
        }

        return new ResponseEntity<>(tknDTOReadingList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Tkn> save(@RequestBody Tkn tkn) {
        tkn.setCreatingDate(LocalDateTime.now());
        tknService.save(tkn);
        return new ResponseEntity<>(tkn, HttpStatus.OK);
    }


    @GetMapping("{id}")
    public ResponseEntity<TknDTO> findById(@PathVariable int id) {
        TknDTO tknDTO = new TknDTO();
        Tkn tkn = tknService.findById(id);
        tknDTO.setId(tkn.getId());
        tknDTO.setTitle(tkn.getTitle());
        tknDTO.setTknType(tkn.getTknType());
        tknDTO.setCompany(tkn.getCompany());
        tknDTO.setSite(tkn.getSite());
        tknDTO.setContent(tkn.getContent());
        tknDTO.setSummaryContent(tkn.getSummaryContent());
        tknDTO.setCreator(tkn.getCreator());
        tknDTO.setCreatingDate(tkn.getCreatingDate());
        tknDTO.setReadingList(tkn.getReadingList());
        tknDTO.setCommentList(tkn.getCommentList());

        return new ResponseEntity<>(tknDTO, HttpStatus.OK);
    }

    @GetMapping("isvisible/{id}")
    public ResponseEntity<String> isVisible(@PathVariable int id){
        Tkn tkn = tknService.findById(id);
        tkn.setIsVisible(!tkn.getIsVisible());
        tknService.save(tkn);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search/{username}")
    public List<TknDTOReading> searchByKeyword(@PathVariable String username, @RequestParam String keyword, @RequestParam Optional<Integer> tknTypeId,@RequestParam Optional<Integer> companyId, @RequestParam Optional<Integer> siteId) {
        List<Tkn> tknList = tknService.searchByKeyword(keyword, tknTypeId,companyId, siteId);
        List<TknDTOReading> tknDTOReadingList = new ArrayList<>();

        for (int i = 0; i < tknList.size(); i ++){
            boolean isRead = false;
            TknDTOReading tknDTOReading = new TknDTOReading();

            tknDTOReading.setId(tknList.get(i).getId());
            tknDTOReading.setTitle(tknList.get(i).getTitle());
            tknDTOReading.setSummaryContent(tknList.get(i).getSummaryContent());
            tknDTOReading.setContent(tknList.get(i).getContent());
            tknDTOReading.setCreator(tknList.get(i).getCreator());
            tknDTOReading.setCreatingDate(tknList.get(i).getCreatingDate());
            tknDTOReading.setTknType(tknList.get(i).getTknType());
            tknDTOReading.setCompany(tknList.get(i).getCompany());
            tknDTOReading.setSite(tknList.get(i).getSite());
            tknDTOReading.setReadingList(tknList.get(i).getReadingList());
            tknDTOReading.setCommentList(tknList.get(i).getCommentList());


            for (int j = 0; j < tknList.get(i).getReadingList().size(); j ++){
                if (tknList.get(i).getReadingList().get(j).getAccount().getUsername().equals(username)) {
                    isRead = true;
                    break;
                };
            }
            tknDTOReading.setRead(isRead);

            tknDTOReadingList.add(tknDTOReading);
        }
        return tknDTOReadingList;
    }




}
