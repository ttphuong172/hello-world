package com.example.helloworld.model.dto;

import com.example.helloworld.model.Comment;
import com.example.helloworld.model.Reading;
import com.example.helloworld.model.TknType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TknDTO {
    private int id;
    private String title;
    private String content;
    private String summaryContent;
    private String creator;
    private LocalDateTime creatingDate;
    private List<Reading> readingList;
    private List<Comment> commentList;
    private TknType tknType;
}
