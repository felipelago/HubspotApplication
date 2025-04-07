package com.hubspot.test.HubspotApplication.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/webhooks/hubspot")
public class WebhookController {

    @PostMapping
    public ResponseEntity<Void> receberWebhook(@RequestBody String payload) {
        log.info("Webhook recebido: {}", payload);
        return ResponseEntity.ok().build();
    }
}