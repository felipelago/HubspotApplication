package com.hubspot.test.HubspotApplication.service;

import com.hubspot.test.HubspotApplication.dto.ContactRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class HubspotService {

    @Value("${hubspot.api.url}")
    private String hubspotApiUrl;

    public ResponseEntity<String> criarContato(ContactRequest contact, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> properties = new HashMap<>();
        properties.put("email", contact.email());
        properties.put("firstname", contact.firstName());
        properties.put("lastname", contact.lastName());

        Map<String, Object> body = Map.of("properties", properties);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        return restTemplate.postForEntity(hubspotApiUrl, request, String.class);
    }
}
