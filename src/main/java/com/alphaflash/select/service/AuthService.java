package com.alphaflash.select.service;

import com.alphaflash.select.SelectConstants;
import com.alphaflash.select.dto.AuthRequest;
import com.alphaflash.select.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;

public class AuthService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public AuthResponse authenticate(String username, String password) throws IOException {

        HttpPost post = new HttpPost(SelectConstants.AUTH_URL);
        post.setEntity(
                new StringEntity(
                        objectMapper.writeValueAsString(
                                new AuthRequest(username, password)
                        ),
                        ContentType.APPLICATION_JSON
                )
        );

        HttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() != 200){
            throw new RuntimeException("Authentication failed");
        }

        return objectMapper.readValue(response.getEntity().getContent(), AuthResponse.class);
    }
}
