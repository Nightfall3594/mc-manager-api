package com.example.api_servers_nightfall_is_a_dev.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpClientConfig {

    @Bean
    public HttpClient httpClient(){
        return HttpClient.newBuilder().build();
    }
}
