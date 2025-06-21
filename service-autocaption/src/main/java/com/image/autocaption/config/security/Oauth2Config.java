package com.image.autocaption.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.google")
public class Oauth2Config {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    public String getClientId() { return clientId; }

    public String getClientSecret() { return clientSecret; }

    public String getRedirectUri() { return redirectUri; }

    public Oauth2Config setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    public Oauth2Config setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    public Oauth2Config setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }
}
