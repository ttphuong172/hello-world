package com.example.helloworld.service.impl;

import com.example.helloworld.model.Contact;
import com.example.helloworld.repository.ContactRepository;
import com.example.helloworld.service.ContactService;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    private final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    @Override
    public Contact saveContact(Contact contact) {
        contact.setOfficePhoneNumber(formatPhoneNumber(contact.getOfficePhoneNumber(), contact.getCountryCode()));
        System.out.println(contact.getOfficePhoneNumber());
        return contactRepository.save(contact);
    }

//    @Override
//    public Contact saveContact(Contact contact) throws Exception {
//        // Validate and format office phone number
//        if (contact.getOfficePhoneNumber() != null && !contact.getOfficePhoneNumber().isEmpty()) {
//            contact.setOfficePhoneNumber(formatPhoneNumber(contact.getOfficePhoneNumber()));
//        }
//
//        // Validate and format mobile phone number
//        if (contact.getMobilePhoneNumber() != null && !contact.getMobilePhoneNumber().isEmpty()) {
//            contact.setMobilePhoneNumber(formatPhoneNumber(contact.getMobilePhoneNumber()));
//        }
//        System.out.println(contact.getMobilePhoneNumber());
//        System.out.println(contact.getMobilePhoneNumber());
//
//        return contactRepository.save(contact);
//    }

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
                System.err.println("Invalid phone number detected: " + phoneNumber);
                return phoneNumber; // Keep original if invalid to allow frontend to show error
            }
        } catch (NumberParseException e) {
            // Log the error and return the original number if parsing fails
            System.err.println("Could not parse phone number: " + phoneNumber + " Error: " + e.getMessage());
            return phoneNumber; // Keep original if parsing fails
        }
    }
}
