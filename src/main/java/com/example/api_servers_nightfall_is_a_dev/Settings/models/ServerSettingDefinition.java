package com.example.api_servers_nightfall_is_a_dev.Settings.models;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@NoArgsConstructor
public class ServerSettingDefinition {

    private static final Map<String, FieldType> KEY_TYPE_MAP = Map.ofEntries(
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

    private static final Map<String, List<String>> ALLOWED_ENUM_OPTIONS = Map.ofEntries(
            Map.entry("gamemode", List.of("survival", "creative", "adventure", "spectator")),
            Map.entry("difficulty", List.of("peaceful", "easy", "normal", "hard"))
    );


    /**
     * Check if the provided key is allowed (and valid)
     * @param key server setting key
     * @return true if valid, false otherwise
     */
    public boolean containsKey(String key) {
        return KEY_TYPE_MAP.containsKey(key);
    }

    /**
     * Check if the provided value is valid for the given key.
     * @param key server setting key
     * @param value server setting value
     * @return true if valid, false otherwise
     */
    public boolean isValidValue(String key, String value) {
        if (!containsKey(key)) {
            return false;
        }

        FieldType type = KEY_TYPE_MAP.get(key);
        switch (type) {
            case BOOLEAN:
                return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
            case INTEGER:
                try {
                    Integer.parseInt(value);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            case ENUM:
                List<String> options = ALLOWED_ENUM_OPTIONS.getOrDefault(key, List.of());
                return options.contains(value.toLowerCase());
            case STRING:
                return true; // Any string is valid
            default:
                return false;
        }
    }

    // Sanitize the value. Remove leading zeroes (for int), convert to lowercase (for enum), etc.
    private String sanitizeValue(String value, FieldType type) {
        return switch (type) {
            case BOOLEAN -> value.equalsIgnoreCase("true") ? "true" : "false";
            case INTEGER -> String.valueOf(Integer.parseInt(value));
            case ENUM -> value.toLowerCase();
            case STRING -> value;
            default -> throw new IllegalArgumentException("Unsupported FieldType: " + type);
        };
    }

    /**
     * Custom factory method to create ServerSetting
     * This is used to include validation when instantiating ServerSetting objects
     * @param key server setting key
     * @param value server setting value
     * @return server setting object
     */
    public ServerSetting createServerSetting(String key, String value) {
        FieldType type = KEY_TYPE_MAP.get(key);
        List<String> options = ALLOWED_ENUM_OPTIONS.getOrDefault(key, List.of());

        String sanitizedValue = sanitizeValue(value, type);

        return ServerSetting.builder()
                .key(key)
                .value(sanitizedValue)
                .type(type)
                .options(options)
                .build();
    }
}
