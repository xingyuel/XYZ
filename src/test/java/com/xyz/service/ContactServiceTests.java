package com.xyz.service;

import com.xyz.entities.Contact;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContactServiceTests {

    @Autowired
    private ContactService service;

    Contact cont1;
    Contact cont2;

    @Before
    public void init() {
        // clear DB
        service.getAll().forEach(ent -> service.delete(ent));
        
        // add four records
        Contact entity = new Contact("Xingyue", "Li", "xli@gogoair.com", "111 N. Canal St.", "Suite 1500", "Chicago", "IL", 60606, "USA", "630-706-0567");
        cont1 = service.save(entity);

        entity = new Contact("Andrew", "Pekin", "apekin@teksystems.com", "111 N. Canal St.", "Suite 300", "Chicago", "IL", 60606, "USA", "847-706-0567");
        cont2 = service.save(entity);

        entity = new Contact("Peter", "Li", "lithal06yahoo.com", "1901 Wellington Rd.", null, "Woodridge", "IL", 60517, "USA", "630-706-0569");
        service.save(entity);

        entity = new Contact("Andrew", "Zhao", "fake.com", "1901 Wellington Rd.", null, "Woodridge", "IL", 60517, "USA", "630-706-1768");
        service.save(entity);
    }

    @Test
    public void testUpdate() {
        // with id
        cont1.setLastName("Last");
        service.update(cont1);
        List<Contact> bufs = service.findByLastName("Last");
        assertEquals(bufs.size(), 1);
        assertEquals(bufs.get(0), cont1);

        // without id, same email
        cont2.setId(null);
        cont2.setLastName("Last2");
        service.update(cont2);
        bufs = service.findByLastName("Last2");
        assertEquals(bufs.size(), 1);
        assertEquals(bufs.get(0), cont2);

        // without id, different email
        Contact entity = new Contact("Andrew", "Last2", "fake@teksystems.com", "121 N. Canal St.", null, "Rockford", "IL", 60106, "USA", "815-555-0567");
        service.save(entity);
        bufs = service.findByLastName("Last2");
        assertEquals(bufs.size(), 2);
        assertEquals(bufs.get(0).getFirstName(), bufs.get(1).getFirstName());   // when id is null and email is different, use both first and last names to update

        cont2.setEmail("newmail@yahoo.com");
        entity = service.update(cont2);
        assertEquals(entity, cont2);
    }

    @Test
    public void testDelete() {
        // with id
        service.delete(cont1);
        List<Contact> bufs = service.getAll();
        assertEquals(3, bufs.size());

        service.delete(cont1);      // cont1 not in DB
        assertEquals(3, bufs.size());

        // without id
        cont2.setId(null);
        service.delete(cont2);
        bufs = service.getAll();
        assertEquals(2, bufs.size());

        service.delete(cont2);      // cont2 not in DB
        bufs = service.getAll();
        assertEquals(2, bufs.size());
    }

    @Test
    public void testReads() {
        // getAll()
        List<Contact> bufs = service.getAll();
        
        assertEquals(bufs.size(), 4);
        Contact _cont1 = bufs.get(0);
        assertEquals(_cont1, cont1);

        Contact _cont2 = bufs.get(1);
        assertEquals(_cont2, cont2);

        // findByFirstName()
        bufs = service.findByFirstName("Andrew");
        assertEquals(bufs.size(), 2);
        _cont1 = bufs.get(0);
        assertEquals(_cont1, cont2);

        bufs = service.findByFirstName("Xingyue");
        assertEquals(bufs.size(), 1);
        _cont1 = bufs.get(0);
        assertEquals(_cont1, cont1);

        // findByLstName()
        bufs = service.findByLastName("Li");
        assertEquals(bufs.size(), 2);
        _cont1 = bufs.get(0);
        assertEquals(_cont1, cont1);

        bufs = service.findByLastName("Pekin");
        assertEquals(bufs.size(), 1);
        _cont1 = bufs.get(0);
        assertEquals(_cont1, cont2);

        // findByZipCoe()
        bufs = service.findByZipCode(60606);
        assertEquals(bufs.size(), 2);
        _cont1 = bufs.get(0);
        assertEquals(_cont1, cont1);
        _cont2 = bufs.get(1);
        assertEquals(_cont2, cont2);
    }

    @Test
    public void testSave() {
        Contact entity = new Contact("First", "Last", "you@gogoair.com", "111 N. Canal St.", "Suite 1500", "Chicago", "IL", 60606, "USA", "630-706-0567");
        service.save(entity);
        List<Contact> bufs = service.findByZipCode(60606);

        assertEquals(bufs.size(), 3);
        assertEquals(bufs.get(2), entity);
    }
}
