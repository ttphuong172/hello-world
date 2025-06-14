package com.example.helloworld.repository;

import com.example.helloworld.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
    List<Contact> findAllBySite_Company_Id(int id);
}
