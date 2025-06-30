package com.example.helloworld.model.dto;

import com.example.helloworld.model.Company;
import com.example.helloworld.model.Contact;
import com.example.helloworld.model.Country;
import com.example.helloworld.model.Line;
import lombok.Data;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Data
public class SiteDTO {
    private int id;
    private String name;
    private String nation;

    @ManyToOne
    @JoinColumn
    private Country country;

    private String city;
    private String address;
//    private String gmt;
    private String zoneId;
    private String configure;
    private String configureKr;
    private Company company;
    private String ric;
    List<Line> lineList;
    List<Contact> contactList;
}
