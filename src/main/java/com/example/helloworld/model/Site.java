package com.example.helloworld.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "site")
    @JsonIgnore
    List<Task> taskList;

    @ManyToOne
    @JoinColumn
    private Company company;

    private String ric;

    @OneToMany(mappedBy = "site")
    @JsonIgnore
    List<Line> lineList;

    @OneToMany(mappedBy = "site")
    @JsonIgnore
    List<Contact> contactList;


    private String topoPath;
}
