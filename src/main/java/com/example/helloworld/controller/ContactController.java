package com.example.helloworld.controller;

import com.example.helloworld.model.Contact;
import com.example.helloworld.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/contacts")
@CrossOrigin
public class ContactController {
    @Autowired
    private ContactService contactService;
//    @PostMapping("")
//    public ResponseEntity<Contact> save(@RequestBody Contact contact) {
//        contactService.saveContact(contact);
//        return new ResponseEntity<>(contact, HttpStatus.OK);
//    }

    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody Contact contact) {
        try {
            Contact savedContact = contactService.saveContact(contact);
            return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<Contact> findById(@PathVariable int id) {
        return new ResponseEntity<>(contactService.findById(id), HttpStatus.OK);
    }

//    @PutMapping("{id}")
//    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Contact contact) {
//        Contact contactCurrent = contactService.findById(id);
//        if (contactCurrent == null) {
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        }
//        contactCurrent.setName(contact.getName());
//        contactCurrent.setEmail(contact.getEmail());
//        contactCurrent.setOfficePhoneNumber(contact.getOfficePhoneNumber());
//        contactCurrent.setMobilePhoneNumber(contact.getMobilePhoneNumber());
//        contactCurrent.setRole(contact.getRole());
//        contactCurrent.setNote(contact.getNote());
//        contactService.save(contactCurrent);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @GetMapping("company/{id}")
    public ResponseEntity<List<Contact>> findAllBySite_Company_Id(@PathVariable int id) {
        return new ResponseEntity<>(contactService.findAllBySite_Company_Id(id), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        Contact contact = contactService.findById(id);
        contactService.delete(contact);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
