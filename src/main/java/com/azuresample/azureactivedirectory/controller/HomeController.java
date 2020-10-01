package com.azuresample.azureactivedirectory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @PreAuthorize("hasRole('Users')")
    @GetMapping("/")
    public String index(Model model, OAuth2AuthenticationToken authentication) {
        final OAuth2AuthorizedClient authorizedClient =
                this.authorizedClientService.loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());
        model.addAttribute("userName", authentication.getName());
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());

        // TODO MVC
        return "User: " + authentication.getName() + "</br>"
                + "Issued At: " + authorizedClient.getAccessToken().getIssuedAt().toString() + "</br>"
                + "Expired At: " + authorizedClient.getAccessToken().getExpiresAt().toString() + "</br>"
                + "Access Token: " + authorizedClient.getAccessToken().getTokenValue();
    }
}
