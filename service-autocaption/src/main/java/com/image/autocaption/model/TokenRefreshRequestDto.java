package com.image.autocaption.model;

import java.util.Objects;

/**
 * The API representation of a token refresh request to generate fresh access JWT tokens.
 */
public class TokenRefreshRequestDto {

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public TokenRefreshRequestDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenRefreshRequestDto that = (TokenRefreshRequestDto) o;
        return Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(refreshToken);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TokenRefreshRequestDto{");
        sb.append("refreshToken='").append(refreshToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
