package com.hubspot.test.HubspotApplication.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

    @Value("${hubspot.client-id}")
    private String clientId;

    @Value("${hubspot.redirect-uri}")
    private String redirectUri;

    @Value("${hubspot.scope}")
    private String scope;

    @Value("${hubspot.auth-url}")
    private String authUrl;

    @GetMapping("/authorize-url")
    public ResponseEntity<String> getAuthorizationUrl() {
        String url = UriComponentsBuilder.newInstance()
                .uri(URI.create(authUrl))
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", scope)
                .queryParam("response_type", "code")
                .build()
                .toUriString();

        return ResponseEntity.ok(url);
    }
}
