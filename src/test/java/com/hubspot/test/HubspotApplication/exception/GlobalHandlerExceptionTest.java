package com.hubspot.test.HubspotApplication.exception;

import com.hubspot.test.HubspotApplication.dto.ApiErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GlobalHandlerExceptionTest {

    private GlobalHandlerException globalHandlerException;

    @BeforeEach
    public void setup() {
        globalHandlerException = new GlobalHandlerException();
    }

    @Test
    public void testHandleContatoJaCadastrado() {
        String errorMessage = "Contato já cadastrado";
        ContatoJaCadastradoException ex = new ContatoJaCadastradoException(errorMessage);

        ResponseEntity<Map<String, String>> response = globalHandlerException.handleContatoJaCadastrado(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    public void testHandleValidationException() {
        Object target = new Object();
        BindingResult bindingResult = new BeanPropertyBindingResult(target, "target");
        bindingResult.addError(new FieldError("target", "email", "Email inválido"));

        // Para criar o MethodArgumentNotValidException, usei um MethodParameter mockado
        MethodParameter parameter = Mockito.mock(MethodParameter.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ApiErrorResponse> response = globalHandlerException.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiErrorResponse apiErrorResponse = response.getBody();
        assert apiErrorResponse != null;
        assertEquals(400, apiErrorResponse.status());
        assertEquals("Erro de validação", apiErrorResponse.error());
        assertEquals("Email inválido", apiErrorResponse.fields().get("email"));
    }

    @Test
    public void testHandleRateLimitExceded() {
        String errorMessage = "Limite de requisições atingido. Tente novamente em instantes.";
        RateLimitExceededException ex = new RateLimitExceededException(errorMessage);

        ResponseEntity<Map<String, String>> response = globalHandlerException.handleRateLimitExceded(ex);

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, response.getStatusCode());
        assertEquals(errorMessage, Objects.requireNonNull(response.getBody()).get("error"));
    }

    @Test
    public void testHandleGenericException() {
        String errorMessage = "Erro inesperado";
        Exception ex = new Exception(errorMessage);

        ResponseEntity<ApiErrorResponse> response = globalHandlerException.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiErrorResponse apiErrorResponse = response.getBody();
        assert apiErrorResponse != null;
        assertEquals(500, apiErrorResponse.status());
        assertEquals("Erro interno: " + errorMessage, apiErrorResponse.error());
        assertNull(apiErrorResponse.fields());
    }

    @Test
    public void testHandleHttpClientErrorException() {
        HttpClientErrorException.Unauthorized ex = (HttpClientErrorException.Unauthorized) HttpClientErrorException.Unauthorized.create(
                "Unauthorized", HttpStatus.UNAUTHORIZED, "Unauthorized", null, null, null);

        ResponseEntity<ApiErrorResponse> response = globalHandlerException.handleHttpClientErrorException(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiErrorResponse apiErrorResponse = response.getBody();
        assert apiErrorResponse != null;
        assertEquals(401, apiErrorResponse.status());
        assertEquals("Token expirado: " + ex.getStatusText(), apiErrorResponse.error());
        assertNull(apiErrorResponse.fields());
    }
}