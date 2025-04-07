package com.hubspot.test.HubspotApplication.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OAuthControllerTest {

    private OAuthController controller;

    @BeforeEach
    public void setUp() {
        controller = new OAuthController();
        ReflectionTestUtils.setField(controller, "clientId", "clientIdMock");
        ReflectionTestUtils.setField(controller, "redirectUri", "http://localhost:8080/callback");
        ReflectionTestUtils.setField(controller, "scope", "contacts");
        ReflectionTestUtils.setField(controller, "authUrl", "http://authUrl");
    }

    @Test
    public void testGetAuthorizationUrl() {
        // Act
        ResponseEntity<String> response = controller.getAuthorizationUrl();
        String url = response.getBody();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assert url != null;
        assertTrue(url.contains("client_id=clientIdMock"), "A URL deve conter o client_id correto");
        assertTrue(url.contains("redirect_uri=http://localhost:8080/callback"), "A URL deve conter o redirect_uri correto");
        assertTrue(url.contains("scope=contacts"), "A URL deve conter o scope correto");
        assertTrue(url.contains("response_type=code"), "A URL deve conter o response_type correto");
    }
}