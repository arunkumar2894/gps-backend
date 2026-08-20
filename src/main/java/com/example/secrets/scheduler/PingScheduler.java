package com.example.secrets.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PingScheduler {
    private static final Logger logger = LoggerFactory.getLogger(PingScheduler.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${api.url}")
    private String apiUrl;

    /**
     * Runs every 5 minutes at 0 seconds (cron format: sec min hour day month day-of-week)
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public void pingTarget() {
        String finalUrl = apiUrl + "/heartbeat";
        try {
            ResponseEntity<String> resp = restTemplate.getForEntity(finalUrl, String.class);
            logger.info("Ping to {} returned status {}", finalUrl, resp.getStatusCodeValue());
        } catch (Exception ex) {
            logger.warn("Ping to {} failed: {}", finalUrl, ex.getMessage());
        }
    }
}
