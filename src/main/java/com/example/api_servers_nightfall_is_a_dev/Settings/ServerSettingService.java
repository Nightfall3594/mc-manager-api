package com.example.api_servers_nightfall_is_a_dev.Settings;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


@Service
@AllArgsConstructor
public class ServerSettingService {

    private static Map<String, FieldType> ALLOWED_KEYS = Map.ofEntries(
            Map.entry("gamemode", FieldType.ENUM),
            Map.entry("difficulty", FieldType.ENUM),
            Map.entry("hardcore", FieldType.BOOLEAN),
            Map.entry("force-gamemode", FieldType.BOOLEAN),
            Map.entry("allow-flight", FieldType.BOOLEAN),
            Map.entry("spawn-protection", FieldType.INTEGER),
            Map.entry("max-players", FieldType.INTEGER),
            Map.entry("view-distance", FieldType.INTEGER),
            Map.entry("simulation-distance", FieldType.INTEGER),
            Map.entry("player-idle-timeout", FieldType.INTEGER),
            Map.entry("max-world-size", FieldType.INTEGER),
            Map.entry("generate-structures", FieldType.BOOLEAN),
            Map.entry("motd", FieldType.STRING),
            Map.entry("hide-online-players", FieldType.BOOLEAN),
            Map.entry("enable-status", FieldType.BOOLEAN),
            Map.entry("white-list", FieldType.BOOLEAN),
            Map.entry("enforce-whitelist", FieldType.BOOLEAN),
            Map.entry("pause-when-empty-seconds", FieldType.INTEGER),
            Map.entry("entity-broadcast-range-percentage", FieldType.INTEGER)
    );

    private static Map<String, List<String>> ALLOWED_ENUM_OPTIONS = Map.ofEntries(
            Map.entry("gamemode", List.of("survival", "creative", "adventure", "spectator")),
            Map.entry("difficulty", List.of("peaceful", "easy", "normal", "hard"))
    );

    public List<ServerSetting> getServerSetting() {

        Path serverPropertiesFile = Path.of("data/minecraft/server.properties");
        List<ServerSetting> serverSettings = new ArrayList<>();
        try {
            Files.readAllLines(serverPropertiesFile, StandardCharsets.UTF_8).stream()
                    .map(line -> line.split("=", 2))
                    .filter(parts -> parts.length == 2)
                    .filter(parts -> ALLOWED_KEYS.containsKey(parts[0]))
                    .forEach(parts -> {

                            String key = parts[0];
                            String value = parts[1];
                            FieldType type = ALLOWED_KEYS.get(key);
                            List<String> options = type == FieldType.ENUM && ALLOWED_ENUM_OPTIONS.containsKey(key)
                                    ? ALLOWED_ENUM_OPTIONS.get(key)
                                    : Collections.emptyList();

                            serverSettings.add(
                                ServerSetting.builder()
                                        .key(key)
                                        .value(value)
                                        .type(type)
                                        .options(options)
                                        .build()
                            );
                    });

        } catch (IOException e) {
            throw new RuntimeException("Error reading server.properties file: " + serverPropertiesFile, e);
        }

        return serverSettings;
    }

    public void updateServerSetting(Map<String, String> newGamerules) {

        Path serverPropertiesFile = Path.of("data/minecraft/server.properties");

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
            for (String key : newGamerules.keySet()) {
                gameRules.put(key, newGamerules.get(key));
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