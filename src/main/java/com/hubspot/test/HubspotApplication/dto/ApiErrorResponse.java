package com.hubspot.test.HubspotApplication.dto;

import java.util.Map;

public record ApiErrorResponse(
        int status,
        String error,
        Map<String, String> fields
) {
}
