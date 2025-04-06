package com.hubspot.test.HubspotApplication.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ContactRequest(
        @Email(message = "Email inválido")
        @NotNull(message = "Email é obrigatório")
        String email,

        String firstName,

        String lastName
) {
}
