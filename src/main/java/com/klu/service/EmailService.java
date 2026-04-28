package com.klu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Value("${brevo.api.key:}")
    private String apiKey;

    @Value("${brevo.sender.email:nikhilmadineedi@gmail.com}")
    private String senderEmail;

    @Value("${brevo.sender.name:Course Sphere}")
    private String senderName;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendEmail(String to, String subject, String text) {

        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_BREVO_API_KEY_HERE")) {
            System.out.println("Email API key not configured. Skipping email...");
            return;
        }

        try {
            String url = "https://api.brevo.com/v3/smtp/email";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.set("api-key", apiKey);

            Map<String, Object> body = new HashMap<>();

            Map<String, String> sender = new HashMap<>();
            sender.put("name", senderName);
            sender.put("email", senderEmail);
            body.put("sender", sender);

            Map<String, String> recipient = new HashMap<>();
            recipient.put("email", to);
            body.put("to", Collections.singletonList(recipient));

            body.put("subject", subject);
            body.put("textContent", text);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            restTemplate.postForEntity(url, request, String.class);
            System.out.println("Email sent successfully to " + to + " via Brevo HTTP API");

        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}