package com.example.helloworld.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String ci;
    private String shortName;
    private String shortName2;

    @ManyToOne
    @JoinColumn
    private Site site;

    @ManyToOne
    @JoinColumn
    private Company company;

    private String isp;

    private String ipAddress;
    private String pingtest;

}
