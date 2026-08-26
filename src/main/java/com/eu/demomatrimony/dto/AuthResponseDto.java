package com.eu.demomatrimony.dto;

public class AuthResponseDto {
    private String token;
    private String tokenType = "Bearer";

    public AuthResponseDto(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTokenType() { return tokenType; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
}