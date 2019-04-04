package com.xyz.repositories;

import com.xyz.entities.Contact;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ContactRepository extends CrudRepository<Contact, Long> {
    @Override
    List<Contact> findAll();

    List<Contact> findByFirstName(String firstName); 
    List<Contact> findByLastName(String lastName); 
    List<Contact> findByZipCode(Integer zipCode); 
    List<Contact> findByEmail(String email);
    List<Contact> findByLastNameAndFirstName(String lastName, String firstName);
}
