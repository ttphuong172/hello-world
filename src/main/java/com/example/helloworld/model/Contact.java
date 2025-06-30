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
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String countryCodeOfficePhoneNumber;
    private String officePhoneNumber;
    private String countryCodeMobilePhoneNumber;
    private String mobilePhoneNumber;
    private String role;
    private String note;

    @ManyToOne
    @JoinColumn
    private Site site;
}
