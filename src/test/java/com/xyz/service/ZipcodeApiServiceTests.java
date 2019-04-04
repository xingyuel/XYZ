package com.xyz.service;

import com.xyz.exceptions.BadRequestException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZipcodeApiServiceTests {
    @Autowired
    private ZipcodeApiService service;

    @Test
    public void positiveTest() {
        boolean flag = service.validate("Chicago", "IL", 60606);
        assertTrue(flag);
    }

    @Test
    public void negativeTest() {
        try {
            service.validate("Chicago", "IL", 55606);
            fail();
        } catch (BadRequestException be) {
            assertEquals(be.getMessage(), "55606 does not match any zipcode in Chicago, IL!");
        }
    }
}
