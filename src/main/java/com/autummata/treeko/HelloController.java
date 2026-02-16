package com.autummata.treeko;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A simple controller to return a friendly greeting.
 */
@RestController
public class HelloController {

    /**
     * Get a friendly greeting.
     * 
     * @return A string containing a friendly greeting.
     */
    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getHello() {
        return Map.of("message", "Hello world");
    }
}
