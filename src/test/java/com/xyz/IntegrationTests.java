package com.xyz;

import com.xyz.dto.ContactDTO;
import com.xyz.entities.Contact;
import com.xyz.service.ContactService;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests {

    static ContactDTO dto = new ContactDTO("Xingyue", "Li", null, "111 N. Canal St.", "Suite 1500", "Chicago", "IL", 60606, "USA", "630-706-0567");

    @LocalServerPort
    private int port;

    @Autowired
    private ContactService service;
    
//  TestRestTemplate restTemplate = new TestRestTemplate();     Note: TestRestTemplate does not cause validation exception
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void init() {
        service.getAll().forEach(entity -> service.delete(entity));
        
        dto = new ContactDTO("Xingyue", "Li", null, "111 N. Canal St.", "Suite 1500", "Chicago", "IL", 60606, "USA", "630-706-0567");
    }

    @Test
    public void negativeIntegrationTest() {
        HttpEntity<ContactDTO> entity = new HttpEntity<>(dto, headers);

        testValidationFailure(entity, HttpMethod.POST);
        testValidationFailure(entity, HttpMethod.PUT);
        testValidationFailure(entity, HttpMethod.DELETE);

        // 54321 is not Chicago's zipcode
        dto.setZipCode(54321);
        entity = new HttpEntity<>(dto, headers);
        testValidationFailure(entity, HttpMethod.POST);
        testValidationFailure(entity, HttpMethod.PUT);
        testValidationFailure(entity, HttpMethod.DELETE);
    }

    @Test
    public void positiveIntegrationTest() {
        // POST
        dto.setEmail("xli@gmail.com");
        testNormal(dto, HttpMethod.POST);

        List<Contact> all = service.getAll();
        assertEquals(all.size(), 1);
        Contact cont = all.get(0);
        assertEquals("Xingyue", cont.getFirstName());
        assertEquals("Li", cont.getLastName());

        // PUT
        dto.setFirstName("Daniel");
        testNormal(dto, HttpMethod.PUT);
        all = service.getAll();
        assertEquals(all.size(), 1);
        cont = all.get(0);
        assertEquals("Daniel", cont.getFirstName());

        // DELETE
        testNormal(dto, HttpMethod.DELETE);
        all = service.getAll();
        assertTrue(all.isEmpty());
        
        // deletion does hot create a new record
        testNormal(dto, HttpMethod.DELETE);
        all = service.getAll();
        assertTrue(all.isEmpty());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private void testValidationFailure(HttpEntity<ContactDTO> entity, HttpMethod method) {
        try {
            restTemplate.exchange(createURLWithPort("/Contact/v1"), method, entity, Object.class);

            fail();
        } catch (RestClientException ex) {
            assertEquals(ex.getMessage(), "400 null");
        }
    }

    private void testNormal(ContactDTO dto, HttpMethod method) {
        HttpEntity<ContactDTO> entity = new HttpEntity<>(dto, headers);

        try {
            restTemplate.exchange(createURLWithPort("/Contact/v1"), method, entity, Object.class);
        } catch (RestClientException ex) {
            fail();
        }
    }
}
