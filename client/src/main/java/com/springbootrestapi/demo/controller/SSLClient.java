package com.springbootrestapi.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class SSLClient {

    private final RestTemplate restTemplate;

    @Autowired
    public SSLClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/send-request")
    public ResponseEntity<String> sendRequest() {
        String url = "https://localhost:8443/connect";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response;
    }
}