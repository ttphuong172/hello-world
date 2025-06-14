package com.example.helloworld.service.impl;

import com.example.helloworld.model.Contact;
import com.example.helloworld.repository.ContactRepository;
import com.example.helloworld.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Contact> findAllBySite_Company_Id(int id) {
        return contactRepository.findAllBySite_Company_Id(id);
    }

    @Override
    public void delete(Contact contact) {
        contactRepository.delete(contact);
    }
}
