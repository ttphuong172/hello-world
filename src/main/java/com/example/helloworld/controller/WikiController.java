package com.example.helloworld.controller;

import com.example.helloworld.model.Wiki;
import com.example.helloworld.service.WikiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wikis")
@CrossOrigin
public class WikiController {
    @Autowired
    private WikiService wikiService;

    @GetMapping("")
    public ResponseEntity<List<Wiki>> findAll() {
        List<Wiki> wikiList = wikiService.findAll();
        return new ResponseEntity<>(wikiList, HttpStatus.OK);
    }
    @GetMapping("{id}")
    public ResponseEntity<Wiki> findById(@PathVariable int id) {
        return new ResponseEntity<>(wikiService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/search")
    public List<Wiki> searchNews(@RequestParam String keyword) {
        return wikiService.searchByKeyword(keyword);
    }
}
