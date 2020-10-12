package com.azuresample.azureactivedirectory.controller;

import com.azuresample.azureactivedirectory.helper.GraphHelper;
import com.azuresample.azureactivedirectory.helper.HttpClientHelper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class HomeController {
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    GraphHelper graphHelper;

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

    @PreAuthorize("hasRole('Users')")
    @GetMapping("/me")
    public String me(Model model, OAuth2AuthenticationToken authentication) throws IOException {
        OAuth2AuthorizedClient authorizedClient = this.getAuthorizedClient(authentication);
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        URL url = new URL(graphHelper.getMsGraphEndpointHost() + "v1.0/me");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the appropriate header fields in the request header.
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        String response = HttpClientHelper.getResponseStringFromConn(conn);

        int responseCode = conn.getResponseCode();
        if(responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException(response);
        }

        JSONObject responseObject = HttpClientHelper.processResponse(responseCode, response);
        return responseObject.toString();
        return accessToken;
    }

    private OAuth2AuthorizedClient getAuthorizedClient(OAuth2AuthenticationToken authentication) {
        return this.authorizedClientService.loadAuthorizedClient(
            authentication.getAuthorizedClientRegistrationId(),
            authentication.getName());
    }
}
