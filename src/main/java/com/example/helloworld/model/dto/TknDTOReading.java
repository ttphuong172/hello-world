package com.example.helloworld.model.dto;

import com.example.helloworld.model.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TknDTOReading {
    private int id;
    private String title;
    private String content;
    private String summaryContent;
    private String creator;
    private LocalDateTime creatingDate;
    private List<Reading> readingList;
    private List<Comment> commentList;
    private boolean isRead;
    private TknType tknType;
    private Company company;
    private Site site;

}
