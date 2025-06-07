package com.image.autocaption.model;

public class AuthRequestDto {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public AuthRequestDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequestDto setPassword(String password) {
        this.password = password;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AuthRequestDto{");
        sb.append("username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
