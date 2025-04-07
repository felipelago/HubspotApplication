package com.hubspot.test.HubspotApplication.controller;

import com.hubspot.test.HubspotApplication.dto.ContactRequest;
import com.hubspot.test.HubspotApplication.service.HubspotService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final HubspotService hubspotService;

    public ContactController(HubspotService hubspotService) {
        this.hubspotService = hubspotService;
    }

    @PostMapping
    public ResponseEntity<String> criarContato(
            @RequestBody @Valid ContactRequest request,
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        return hubspotService.criarContato(request, token);
    }
}

