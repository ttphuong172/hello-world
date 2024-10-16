package com.example.helloworld.controller;

import com.example.helloworld.model.Contact;
import com.example.helloworld.model.Line;
import com.example.helloworld.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contacts")
@CrossOrigin
public class ContactController {
    @Autowired
    private ContactService contactService;
    @PostMapping("")
    public ResponseEntity<Contact> save(@RequestBody Contact contact) {
        contactService.save(contact);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Contact> findById(@PathVariable int id) {
        return new ResponseEntity<>(contactService.findById(id), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Contact contact) {
        Contact contactCurrent = contactService.findById(id);
        if (contactCurrent == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        contactCurrent.setName(contact.getName());
        contactCurrent.setEmail(contact.getEmail());
        contactCurrent.setOffice(contact.getOffice());
        contactCurrent.setPhone(contact.getPhone());
        contactCurrent.setRole(contact.getRole());
        contactCurrent.setNote(contact.getNote());
        contactService.save(contactCurrent);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
