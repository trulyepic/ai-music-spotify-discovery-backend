package com.music_discovery.ai_music_dis.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:3001", allowCredentials = "true")
@RestController
@RequestMapping("/api/spotify")
public class SpotifyAuthController {

    @GetMapping("/user")
    public Map<String, Object> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
        return principal.getAttributes(); // Returns Spotify user details
    }

    @GetMapping("/token")
    public String getAccessToken(OAuth2AuthenticationToken authentication) {
        return authentication.getPrincipal().getAttributes().toString(); // Debugging purposes
    }


    @GetMapping("/top-tracks")
    public Map<String, Object> getUserTopTracks(
            @RegisteredOAuth2AuthorizedClient("spotify")OAuth2AuthorizedClient auth2AuthorizedClient
            ){
        String accessToken = auth2AuthorizedClient.getAccessToken().getTokenValue();
        String url = "https://api.spotify.com/v1/me/top/tracks?limit=10";

        RestTemplate restTemplate = new RestTemplate();
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

        return restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, Map.class).getBody();
    }
}
