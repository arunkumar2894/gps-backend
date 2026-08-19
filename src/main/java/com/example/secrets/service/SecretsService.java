package com.example.secrets.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SecretsService {
    private final ConcurrentHashMap<String, String> secrets = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        secrets.put("mapsApiKey", "AIzaSyAR4NS9mtK7_v_YW2WcmRmbai4f1c8j36I");
        secrets.put("deviceUuid", "4b29f3e7-6d5b-4604-b815-af5a6dcdf4d3");
    }

    public Map<String, String> getAll() {
        return Map.copyOf(secrets);
    }

    public boolean update(String api, String token) {
        secrets.put("mapsApiKey", api);
        secrets.put("deviceUuid", token);
        return true;
    }

    public boolean update(String token) {
        secrets.put("deviceUuid", token);
        return true;
    }
}
