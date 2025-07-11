package com.example.helloworld.service;

import com.example.helloworld.model.Contact;

import java.util.List;

public interface ContactService {
    void save(Contact contact);
    Contact findById(int id);
    List<Contact> findAllBySite_Company_Id(int id);
    void delete(Contact contact);
}
