package com.example.helloworld.service;

import com.example.helloworld.model.Contact;

public interface ContactService {
    void save(Contact contact);
    Contact findById(int id);
}
