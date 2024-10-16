package com.example.helloworld.model.dto;

import com.example.helloworld.model.Company;
import com.example.helloworld.model.Contact;
import com.example.helloworld.model.Line;
import lombok.Data;

import java.util.List;

@Data
public class SiteDTO {
    private int id;
    private String name;
    private float gmt;
    private String configure;
    private String configureKr;
    private Company company;
    private String ric;
    List<Line> lineList;
    List<Contact> contactList;
}
