package com.image.autocaption.config.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "google")
public class Outh2Config {

    private String clientId;

    private String clientSecret;

    private String redirectUri;

    public String getClientId() { return clientId; }

    public String getClientSecret() { return clientSecret; }

    public String getRedirectUri() { return redirectUri; }
}
