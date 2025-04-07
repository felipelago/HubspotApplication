package com.hubspot.test.HubspotApplication.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class WebhookControllerTest {

    private WebhookController webhookController;

    @BeforeEach
    public void setUp() {
        webhookController = new WebhookController();
    }

    @Test
    public void testReceberWebhook() {
        // Arrange
        String payload = "{\"key\":\"value\"}";

        // Act
        ResponseEntity<Void> response = webhookController.receberWebhook(payload);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}