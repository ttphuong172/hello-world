package com.example.helloworld.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Sms {
    private Company company;
    private Site site;
    private String network;
    private Line line;
    private Impact impact;
    private Event event;
    private String extra;
    private LocalDateTime issueTime;
    private LocalDateTime restoreTime;
}
