package com.example.api_servers_nightfall_is_a_dev.common;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple key value storage implementation, to be used across the app,
 * just in case something needs to be stored without making network-calls
 */
@Component
@NoArgsConstructor
public class GlobalCache {

    private final Map<String, String> data = new HashMap<>();

    public String get(String key) { return data.get(key); }
    public void set(String key, String value) { data.put(key, value); }
}
