package com.image.autocaption.resource.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.image.autocaption.config.security.Oauth2Config;
import com.image.autocaption.model.dto.AuthResponseDto;
import com.image.autocaption.security.JwtUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/oauth2/callback")
public class OAuth2Resource {

    private final JwtUtil jwtUtil;

    private final RestTemplate restTemplate;

    private final UserDetailsService userDetailsService;

    private final Oauth2Config oauth2Config;

    public OAuth2Resource(JwtUtil jwtUtil,
                          RestTemplate restTemplate,
                          UserDetailsService userDetailsService, Oauth2Config oauth2Config) {
        this.jwtUtil = jwtUtil;
        this.restTemplate = restTemplate;
        this.userDetailsService = userDetailsService;
        this.oauth2Config = oauth2Config;
    }

    @GetMapping("/google")
    public ResponseEntity<AuthResponseDto> googleOauth2Callback(@RequestParam("code") String authorizationCode) {

        System.out.println("Hellloooo");
        try {
            // 1. Exchange authorization code for tokens
            Map<String, Object> tokenResponse = exchangeCodeForTokens(authorizationCode);
            String idTokenString = (String) tokenResponse.get("id_token");

            // 2. Verify ID Token
            GoogleIdToken idToken = verifyIdToken(idTokenString);
            if (idToken == null) {
                return ResponseEntity.status(UNAUTHORIZED).build();
            }

            // 3. Extract email
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            // 4. Load user using default Spring UserDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            System.out.println(userDetails);
            // 5. Generate JWT tokens
            String accessToken = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            return ResponseEntity.ok(new AuthResponseDto(accessToken, refreshToken));

        } catch (UsernameNotFoundException e) {
//            return ResponseEntity.status(UNAUTHORIZED).body("User not registered: " + e.getMessage());
            return ResponseEntity.status(UNAUTHORIZED).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OAuth2 login failed: " + e.getMessage());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).build();
        }
    }

    private Map<String, Object> exchangeCodeForTokens(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("code", code);
        form.add("client_id", oauth2Config.getClientId());
        form.add("client_secret", oauth2Config.getClientSecret());
        form.add("redirect_uri", oauth2Config.getRedirectUri());
        form.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, Map.class);
        return response.getBody();
    }

    private GoogleIdToken verifyIdToken(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(oauth2Config.getClientId()))
                .build();

        return verifier.verify(idTokenString);
    }
}
