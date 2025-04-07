package com.hubspot.test.HubspotApplication.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OAuthCallbackControllerTest {

    @Mock
    private RestTemplate restTemplate;

    private OAuthCallbackController controller;

    @BeforeEach
    public void setUp() {
        controller = new OAuthCallbackController();
        ReflectionTestUtils.setField(controller, "clientId", "clientIdMock");
        ReflectionTestUtils.setField(controller, "clientSecret", "clientSecretMock");
        ReflectionTestUtils.setField(controller, "redirectUri", "http://redirect");
        ReflectionTestUtils.setField(controller, "tokenUrl", "http://tokenUrl");
        // Injetando o RestTemplate mockado
        ReflectionTestUtils.setField(controller, "restTemplate", restTemplate);
    }

    @Test
    public void testHandleCallback() {
        String code = "authCode123";
        String responseBody = "access_token=token123";
        ResponseEntity<String> tokenResponse = new ResponseEntity<>(responseBody, HttpStatus.OK);

        // Configurando o comportamento do restTemplate
        when(restTemplate.postForEntity(eq("http://tokenUrl"), any(HttpEntity.class), eq(String.class)))
                .thenReturn(tokenResponse);

        // Act
        ResponseEntity<String> response = controller.handleCallback(code);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseBody, response.getBody());

        ArgumentCaptor<HttpEntity> captor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).postForEntity(eq("http://tokenUrl"), captor.capture(), eq(String.class));

        HttpEntity<MultiValueMap<String, String>> capturedRequest = captor.getValue();
        HttpHeaders headers = capturedRequest.getHeaders();
        MultiValueMap<String, String> form = capturedRequest.getBody();

        assertEquals(MediaType.APPLICATION_FORM_URLENCODED, headers.getContentType());
        assert form != null;
        assertEquals("authorization_code", form.getFirst("grant_type"));
        assertEquals("clientIdMock", form.getFirst("client_id"));
        assertEquals("clientSecretMock", form.getFirst("client_secret"));
        assertEquals("http://redirect", form.getFirst("redirect_uri"));
        assertEquals(code, form.getFirst("code"));
    }
}