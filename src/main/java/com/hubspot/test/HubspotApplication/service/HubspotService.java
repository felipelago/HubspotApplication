package com.hubspot.test.HubspotApplication.service;

import com.hubspot.test.HubspotApplication.dto.ContactRequest;
import com.hubspot.test.HubspotApplication.exception.ContatoJaCadastradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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

        try {
            return restTemplate.postForEntity(hubspotApiUrl, request, String.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new ContatoJaCadastradoException("Contato já está cadastrado no HubSpot.");
            }
            throw e;
        }
    }
}
