package com.example.secrets.controller;

import com.example.secrets.service.SecretsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class GetLocationController {

    @Autowired
    SecretsService secretsService;

    @Value("${get.location.gps.url}")
    private String locationGPSUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/location")
    public ResponseEntity<Object> getLocation() {
        String originalGpsUrl = locationGPSUrl + secretsService.getAll().get("deviceUuid");

        try {
            Object locationData = restTemplate.getForObject(originalGpsUrl, Object.class);
            return ResponseEntity.ok(locationData);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
