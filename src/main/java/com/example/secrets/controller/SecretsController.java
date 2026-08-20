package com.example.secrets.controller;

import com.example.secrets.service.SecretsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/secrets")
public class SecretsController {
    private final SecretsService service;
    private final String updateToken;

    public SecretsController(SecretsService service, @Value("${app.update.token}") String updateToken) {
        this.service = service;
        this.updateToken = updateToken;
    }

//    @GetMapping
//    public ResponseEntity<Map<String, String>> getSecrets() {
//        return ResponseEntity.ok(service.getAll());
//    }

    /**
     * Update a specific secret identified by {api} (mapsApiKey or deviceUuid).
     * Path variables: {token} (must match app.update.token), {api} (key name)
     * Request body JSON: { "value": "new-value" }
     */
    @GetMapping("/{updateToken}/{token}/{api}")
    public ResponseEntity<?> updateSecret(@PathVariable String updateToken,
                                          @PathVariable String token,
                                          @PathVariable String api) {
        if (!this.updateToken.equals(updateToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid token"));
        }
        boolean ok = service.update(api, token);
        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid api key"));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{updateToken}/{token}")
    public ResponseEntity<?> updateSecret(@PathVariable String updateToken,
                                          @PathVariable String token) {
        if (!this.updateToken.equals(updateToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "invalid token"));
        }
        boolean ok = service.update(token);
        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of("error", "invalid api key"));
        }
        return ResponseEntity.ok(service.getAll());
    }
}
