package com.xyz.service;

import com.xyz.entities.Contact;
import com.xyz.misc.Utils;
import com.xyz.repositories.ContactRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

    private final ContactRepository repository;

    @Autowired
    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    // This is only for testing purpose
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Contact> getAll() {
        return repository.findAll();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Contact> findByFirstName(String firstName) {
        return repository.findByFirstName(firstName);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Contact> findByLastName(String lastName) {
        return repository.findByLastName(lastName);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Contact> findByZipCode(Integer zipCode) {
        return repository.findByZipCode(zipCode);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Contact save(Contact entity) {
        if (entity == null) {
            return null;
        }

        return repository.save(entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Contact update(Contact entity) {
        if (entity == null) {
            return null;
        }

        Long id = entity.getId();
        if (id == null) {   // Contact instance from the caller does not have id
            Contact original = getOriginal(entity);
            id = original.getId();

            if (id == null) {
                LOGGER.error("Contact's id is null! Should not happen.");
                return entity;
            }
            Utils.updateEntity(original, entity);

            return original;
        } else {    // with a valid id
            return repository.save(entity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void delete(Contact entity) {
        if (entity == null) {
            return;
        }

        if (entity.getId() == null) {
            entity = getOriginal(entity);
        }

        repository.delete(entity);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    /*
        If a Contact is passed in without the id, update or delete does not work,
        so need to find the original record from DB.

    */
    private Contact getOriginal(Contact entity) {
        List<Contact> orig = repository.findByEmail(entity.getEmail());

        if (orig != null && !orig.isEmpty()) {
            return orig.get(0);
        }

        orig = repository.findByLastNameAndFirstName(entity.getLastName(), entity.getFirstName());
        if (orig == null || orig.isEmpty()) {
            LOGGER.warn("No records found for this full name.");

            return entity;
        }

        if (orig.size() == 1)
            return orig.get(0);

        // orig contains multiple records
        return Utils.findMostSimilar(entity, orig);
    }
}
