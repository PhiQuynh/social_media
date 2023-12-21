package com.example.project.dto;

import java.util.Map;

public class TokenResponse {
    private String accessToken;
    private Map<String, String> error;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenResponse(Map<String, String> error) {
        this.error = error;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Map<String, String> getError() {
        return error;
    }
}