package com.hubspot.test.HubspotApplication.service;

import com.hubspot.test.HubspotApplication.dto.ContactRequest;
import com.hubspot.test.HubspotApplication.exception.ContatoJaCadastradoException;
import com.hubspot.test.HubspotApplication.exception.RateLimitExceededException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class HubspotServiceTest {

    @Test
    public void testCriarContatoSuccess() {
        // Arrange
        HubspotService service = new HubspotService();
        // Injetando a URL da API via ReflectionTestUtils
        ReflectionTestUtils.setField(service, "hubspotApiUrl", "http://api.hubspot.com/contacts");

        ContactRequest contact = new ContactRequest("email@example.com", "John", "Doe");
        String token = "accessToken123";

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Created", HttpStatus.CREATED);

        // Intercepta a criação do RestTemplate e define o comportamento esperado
        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForEntity(eq("http://api.hubspot.com/contacts"),
                    any(HttpEntity.class), eq(String.class)))
                    .thenReturn(responseEntity);
        })) {
            // Act
            ResponseEntity<String> result = service.criarContato(contact, token);

            // Assert
            assertEquals(responseEntity, result);
        }
    }

    @Test
    public void testCriarContatoConflict() {
        // Arrange
        HubspotService service = new HubspotService();
        ReflectionTestUtils.setField(service, "hubspotApiUrl", "http://api.hubspot.com/contacts");

        ContactRequest contact = new ContactRequest("email@example.com", "John", "Doe");
        String token = "accessToken123";

        HttpClientErrorException conflictException = HttpClientErrorException.create(
                HttpStatus.CONFLICT, "Conflict", HttpHeaders.EMPTY, null, null);

        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForEntity(eq("http://api.hubspot.com/contacts"),
                    any(HttpEntity.class), eq(String.class)))
                    .thenThrow(conflictException);
        })) {
            // Act & Assert: Deve lançar ContatoJaCadastradoException
            Exception exception = assertThrows(ContatoJaCadastradoException.class,
                    () -> service.criarContato(contact, token));
            assertEquals("Contato já está cadastrado no HubSpot.", exception.getMessage());
        }
    }

    @Test
    public void testCriarContatoRateLimitExceeded() {
        // Arrange
        HubspotService service = new HubspotService();
        ReflectionTestUtils.setField(service, "hubspotApiUrl", "http://api.hubspot.com/contacts");

        ContactRequest contact = new ContactRequest("email@example.com", "John", "Doe");
        String token = "accessToken123";

        HttpClientErrorException rateLimitException = HttpClientErrorException.create(
                HttpStatus.TOO_MANY_REQUESTS, "Too Many Requests", HttpHeaders.EMPTY, null, null);

        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForEntity(eq("http://api.hubspot.com/contacts"),
                    any(HttpEntity.class), eq(String.class)))
                    .thenThrow(rateLimitException);
        })) {
            // Act & Assert: Deve lançar RateLimitExceededException
            Exception exception = assertThrows(RateLimitExceededException.class,
                    () -> service.criarContato(contact, token));
            assertEquals("Limite de requisições atingido. Tente novamente em instantes.", exception.getMessage());
        }
    }

    @Test
    public void testCriarContatoOtherHttpClientErrorException() {
        // Arrange
        HubspotService service = new HubspotService();
        ReflectionTestUtils.setField(service, "hubspotApiUrl", "http://api.hubspot.com/contacts");

        ContactRequest contact = new ContactRequest("email@example.com", "John", "Doe");
        String token = "accessToken123";

        HttpClientErrorException otherException = HttpClientErrorException.create(
                HttpStatus.BAD_REQUEST, "Bad Request", HttpHeaders.EMPTY, null, null);

        try (MockedConstruction<RestTemplate> mocked = Mockito.mockConstruction(RestTemplate.class, (mock, context) -> {
            when(mock.postForEntity(eq("http://api.hubspot.com/contacts"),
                    any(HttpEntity.class), eq(String.class)))
                    .thenThrow(otherException);
        })) {
            // Act & Assert: Qualquer outro HttpClientErrorException deve ser repassado
            HttpClientErrorException thrown = assertThrows(HttpClientErrorException.class,
                    () -> service.criarContato(contact, token));
            assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        }
    }
}
