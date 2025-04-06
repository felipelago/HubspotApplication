package com.hubspot.test.HubspotApplication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/hubspot")
public class WebhookController {

    @PostMapping
    public ResponseEntity<Void> receberWebhook(@RequestBody String payload) {
        System.out.println("Webhook recebido: " + payload);
        return ResponseEntity.ok().build();
    }
}