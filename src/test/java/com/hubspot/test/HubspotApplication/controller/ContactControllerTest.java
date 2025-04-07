package com.hubspot.test.HubspotApplication.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hubspot.test.HubspotApplication.dto.ContactRequest;
import com.hubspot.test.HubspotApplication.service.HubspotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ContactControllerTest {

    @Mock
    private HubspotService hubspotService;

    @InjectMocks
    private ContactController contactController;

    @Test
    public void testCriarContato() {
        ContactRequest request = new ContactRequest("felipe@teste.com", "felipe", "teste");

        String authHeader = "Bearer token123";
        String tokenEsperado = "token123";

        ResponseEntity<String> respostaEsperada = ResponseEntity.ok("Contato criado");

        // Simula o comportamento do hubspotService
        when(hubspotService.criarContato(request, tokenEsperado)).thenReturn(respostaEsperada);

        // Act
        ResponseEntity<String> resposta = contactController.criarContato(request, authHeader);

        // Assert
        assertEquals(respostaEsperada, resposta);
        verify(hubspotService).criarContato(request, tokenEsperado);
    }
}