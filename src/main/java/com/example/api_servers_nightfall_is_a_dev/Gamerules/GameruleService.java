package com.example.api_servers_nightfall_is_a_dev.Gamerules;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
@AllArgsConstructor
public class GameruleService {

    private static final Set<String> ALLOWED_KEYS = new HashSet<>(Set.of(
            "gamemode",
            "difficulty",
            "hardcore",
            "force-gamemode",
            "allow-flight",
            "spawn-protection",
            "max-players",
            "view-distance",
            "simulation-distance",
            "player-idle-timeout",
            "max-world-size",
            "level-name",
            "level-type",
            "generate-structures",
            "motd",
            "hide-online-players",
            "enable-status",
            "white-list",
            "enforce-whitelist",
            "pause-when-empty-seconds",
            "entity-broadcast-range-percentage"
    ));

    public Map<String, String> getGamerules() {

        Path serverPropertiesFile = Path.of("data/minecraft/server.properties");
        HashMap<String, String> propertiesMap = new HashMap<>();
        try {
            Files.readAllLines(serverPropertiesFile, StandardCharsets.UTF_8).stream()
                    .map(line -> line.split("=", 2))
                    .filter(parts -> parts.length == 2)
                    .filter(parts -> ALLOWED_KEYS.contains(parts[0]))
                    .forEach(parts -> propertiesMap.put(parts[0], parts[1]));

        } catch (IOException e) {
            throw new RuntimeException("Error reading server.properties file: " + serverPropertiesFile, e);
        }

        return propertiesMap;
    }
}