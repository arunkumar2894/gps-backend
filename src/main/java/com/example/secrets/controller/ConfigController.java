package com.example.secrets.controller;

import com.example.secrets.service.SecretsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final SecretsService secretsService;
    private final String apiUrl;

    public ConfigController(SecretsService secretsService, @Value("${api.url}") String apiUrl) {
        this.secretsService = secretsService;
        this.apiUrl = apiUrl;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getConfig() {
        // Return non-sensitive configuration only. Do NOT include API keys.
        String deviceUuid = secretsService.getAll().get("deviceUuid");
        return ResponseEntity.ok(Map.of(
                "apiUrl", apiUrl
        ));
    }
}
