package com.example.api_servers_nightfall_is_a_dev.common;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;

/**
 * Utility class to check server status, and other related stuff.
 * When other services require specific server statuses,
 * migrate those methods here.
 */
@Component
@AllArgsConstructor
public class ServerStatus {

    @Autowired
    HttpClient client;

    public boolean isOnline() {
            final String endpoint = System.getenv("METRICS_ENDPOINT");

            if (endpoint == null || endpoint.isBlank()) throw new IllegalStateException("METRICS_ENDPOINT is not set");

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(endpoint))
                        .build();


                HttpResponse<String> result = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

                return result.body().lines()
                        .map(String::trim)
                        .anyMatch(line ->
                                line.contains("minecraft_status_healthy") && line.endsWith(" 1")
                        );

            } catch (Exception e) {
                return false;
            }
    }

    @PreDestroy
    public void closeClient(){
        client.close();
    }
}
