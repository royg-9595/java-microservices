package com.ameyrupji.helloworld.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)  // This will start an embedded web server
public class HelloWorldControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;  // Used for making HTTP requests in tests

    @Test
    public void testHelloWorld() {
        // Make a GET request to /helloworld/
        ResponseEntity<String> response = restTemplate.getForEntity("/helloworld/", String.class);

        // Assert that the response status is 200 OK and the response body is "Hello World!"
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Hello World!", response.getBody());
    }
}
