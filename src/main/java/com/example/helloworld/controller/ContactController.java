package com.example.helloworld.controller;

import com.example.helloworld.model.Contact;
import com.example.helloworld.model.Ping;
import com.example.helloworld.service.ContactService;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
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

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @PostMapping("")
    public  ResponseEntity<?> save (@RequestBody Contact contact){
        try {
            contact.setOfficePhoneNumber(formatPhoneNumber(contact.getOfficePhoneNumber(), contact.getCountryCodeOfficePhoneNumber()));
            contact.setMobilePhoneNumber(formatPhoneNumber(contact.getMobilePhoneNumber(), contact.getCountryCodeMobilePhoneNumber()));
            contactService.save(contact);
            return new ResponseEntity<>(contact, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @GetMapping("{id}")
    public ResponseEntity<Contact> findById(@PathVariable int id) {
        return new ResponseEntity<>(contactService.findById(id), HttpStatus.OK);
    }


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

    // Helper method for phone number formatting and validation
    private String formatPhoneNumber(String phoneNumber, String countryCode) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return ""; // Or null, depending on your preference for empty numbers
        }

        try {
            // Parse the number, using the provided countryCode as a default region
            Phonenumber.PhoneNumber numberProto = phoneNumberUtil.parse(phoneNumber, countryCode);

            // Check if the number is valid
            if (phoneNumberUtil.isValidNumber(numberProto)) {
                // Format to E.164
                return phoneNumberUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
            } else {
                // Log and return the original if invalid, or throw an exception
                throw new IllegalArgumentException("So dien thoai ko dung");
            }
        } catch (NumberParseException e) {
            // Log the error and return the original number if parsing fails
            throw new IllegalArgumentException("\"Could not parse phone number: \" + phoneNumber + \" Error: \" + e.getMessage()");
        }
    }

}
