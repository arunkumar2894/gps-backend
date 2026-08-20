package com.example.secrets.controller;

import com.example.secrets.service.SecretsService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/maps")
public class MapsProxyController {

    private final SecretsService secretsService;
    private static final Pattern CALLBACK_SAFE = Pattern.compile("[A-Za-z0-9_]+");

    public MapsProxyController(SecretsService secretsService) {
        this.secretsService = secretsService;
    }

    @GetMapping(path = "/js", produces = "application/javascript")
    public ResponseEntity<byte[]> getMapsJs(@RequestParam(name = "callback", required = false) String callback) throws Exception {
        Map<String, String> secrets = secretsService.getAll();
        String key = secrets.get("mapsApiKey");

        if (key == null || key.isEmpty()) {
            return ResponseEntity.badRequest().body("/* Missing Google Maps API key on server */".getBytes(StandardCharsets.UTF_8));
        }

        String cb = (callback == null || callback.isEmpty()) ? "initMap" : callback;
        if (!CALLBACK_SAFE.matcher(cb).matches()) {
            return ResponseEntity.badRequest().body("/* Invalid callback */".getBytes(StandardCharsets.UTF_8));
        }

        // Build remote URL
        String encodedKey = URLEncoder.encode(key, StandardCharsets.UTF_8);
        String encodedCallback = URLEncoder.encode(cb, StandardCharsets.UTF_8);
        String remote = String.format("https://maps.googleapis.com/maps/api/js?key=%s&callback=%s", encodedKey, encodedCallback);

        URL url = new URL(remote);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(15000);

        int status = conn.getResponseCode();
        InputStream is = (status >= 200 && status < 400) ? conn.getInputStream() : conn.getErrorStream();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }
        is.close();
        conn.disconnect();

        byte[] body = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf("application/javascript; charset=UTF-8"));
        // Do not cache aggressively; let browsers revalidate if needed
        headers.setCacheControl("no-cache, no-store, must-revalidate");

        return ResponseEntity.status(status).headers(headers).body(body);
    }
}
