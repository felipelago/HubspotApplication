package com.hubspot.test.HubspotApplication.dto;

import jakarta.validation.constraints.Email;

public record ContactRequest(
        @Email
        String email,

        String firstName,

        String lastName
) {
}
