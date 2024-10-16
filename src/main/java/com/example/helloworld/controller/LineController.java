package com.example.helloworld.controller;

import com.example.helloworld.model.Line;
import com.example.helloworld.model.Site;
import com.example.helloworld.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/lines")
@CrossOrigin
public class LineController {
    @Autowired
    private LineService lineService;

    @GetMapping("site/{id}")
    public ResponseEntity<List<Line>> findLinesBySite_IdOrderByName(@PathVariable int id) {
        return new ResponseEntity<>(lineService.findLinesBySite_IdOrderByName(id), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Line> findById(@PathVariable int id) {
        return new ResponseEntity<>(lineService.findById(id), HttpStatus.OK);
    }

     @PostMapping("")
     public ResponseEntity<Line> save(@RequestBody Line line) {
         lineService.save(line);
         return new ResponseEntity<>(line, HttpStatus.OK);
     }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Line line) {
        Line lineCurrent = lineService.findById(id);
        if (lineCurrent == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        lineCurrent.setName(line.getName());
        lineCurrent.setShortName(line.getShortName());
        lineCurrent.setShortName2(line.getShortName2());
        lineCurrent.setIsp(line.getIsp());
        lineCurrent.setCi(line.getCi());
        lineCurrent.setIpAddress(line.getIpAddress());
        lineCurrent.setPingtest(line.getPingtest());

        lineService.save(lineCurrent);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
