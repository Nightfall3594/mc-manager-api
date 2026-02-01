package com.example.api_servers_nightfall_is_a_dev.Settings;

import com.example.api_servers_nightfall_is_a_dev.Settings.models.ServerSetting;
import com.example.api_servers_nightfall_is_a_dev.Settings.models.ServerSettingDefinition;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Service
@AllArgsConstructor
public class ServerSettingService {

    @Autowired
    private final ServerSettingDefinition schema;

    public List<ServerSetting> getServerSetting() {

        Path serverPropertiesFile = Path.of("data/minecraft/server.properties");
        try {
            return Files.readAllLines(serverPropertiesFile, StandardCharsets.UTF_8).stream()
                    .map(line -> line.split("=", 2))
                    .filter(parts -> parts.length == 2)
                    .filter(parts -> schema.containsKey(parts[0]))
                    .map(parts -> schema.createServerSetting(parts[0], parts[1]))
                    .toList();

        } catch (IOException e) {
            throw new RuntimeException("Error reading server.properties file: " + serverPropertiesFile, e);
        }
    }

    public void updateServerSetting(Map<String, String> newGamerules) {

        Path serverPropertiesFile = Path.of("data/minecraft/server.properties");

        List<String> invalidKeys = newGamerules.keySet().stream()
                // filter keys not in schema or have invalid values
                .filter(key -> !schema.containsKey(key) || !schema.isValidValue(key, newGamerules.get(key)))
                .toList();

        if (!invalidKeys.isEmpty()) {
            throw new IllegalArgumentException("Invalid gamerules keys or values: " + String.join(", ", invalidKeys));
        }

        // Validate all new gamerules keys
        List<ServerSetting> validKeys = newGamerules.keySet().stream()
                .map(key -> schema.createServerSetting(key, newGamerules.get(key)))
                .toList();

        Map<String, String> gameRules = new HashMap<>();
        try {
            // get all existing server properties
            Files.readAllLines(serverPropertiesFile).stream()
                    .map(line -> line.split("=", 2))
                    .filter(parts -> !parts[0].startsWith("#"))
                    .filter(parts -> parts.length == 2)
                    .forEach(keyValues -> {
                        gameRules.put(keyValues[0], keyValues[1]);
                    });

            // update with new gamerules
            for (ServerSetting item : validKeys) {
                gameRules.put(item.getKey(), item.getValue());
            }

            // write back to server.properties file
            List<String> linesToWrite = new ArrayList<>();
            for (String key : gameRules.keySet()) {
                linesToWrite.add(key + "=" + gameRules.get(key));
            }
            Files.write(serverPropertiesFile, linesToWrite, StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Error reading server.properties file: " + serverPropertiesFile, e);
        }
    }
}