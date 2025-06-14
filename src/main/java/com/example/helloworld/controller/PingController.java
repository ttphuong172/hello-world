package com.example.helloworld.controller;

import com.example.helloworld.model.Ping;
import com.example.helloworld.service.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/ping")
@CrossOrigin
public class PingController {
    @Autowired
    private PingService pingService;

    @GetMapping("")
    public ResponseEntity<List<Ping>> findAll() {
        List<Ping> pingList = pingService.findAll();
        return new ResponseEntity<>(pingList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Ping> save(@RequestBody Ping ping) {
        ping.setIsUp(Boolean.FALSE);
        pingService.save(ping);
        return new ResponseEntity<>(ping, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable int id) {
        Ping ping = pingService.findById(id);
        pingService.delete(ping);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/audio")
    public ResponseEntity<ClassPathResource> getAudio() {
        // Path to the audio file inside 'src/main/resources/static/audio'
        String musicFilePath = "static/audio/lineup.wav";

        // Use ClassPathResource to access the file in resources
        ClassPathResource resource = new ClassPathResource(musicFilePath);

        // Respond with the audio file and set the appropriate headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)  // Use a generic media type for binary files
                .body(resource);
    }

    @GetMapping("/audioalarm")
    public ResponseEntity<ClassPathResource> getAudioAlarm() {
        // Path to the audio file inside 'src/main/resources/static/audio'
        String musicFilePath = "static/audio/alarm.wav";

        // Use ClassPathResource to access the file in resources
        ClassPathResource resource = new ClassPathResource(musicFilePath);

        // Respond with the audio file and set the appropriate headers
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)  // Use a generic media type for binary files
                .body(resource);
    }

}
