package com.example.helloworld.service.impl;

import com.example.helloworld.model.Contact;
import com.example.helloworld.repository.ContactRepository;
import com.example.helloworld.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;
    @Override
    public void save(Contact contact) {
        contactRepository.save(contact);
    }

    @Override
    public Contact findById(int id) {
        return contactRepository.findById(id).orElse(null);
    }
}
