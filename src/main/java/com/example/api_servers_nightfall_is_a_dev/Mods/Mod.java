package com.example.api_servers_nightfall_is_a_dev.Mods;

import lombok.*;


/**
 * Represents a Minecraft mod with its associated metadata.
 * @field fileName: URL encoded file name
 * @field name: Mod name from metadata
 * @field fileSize: Size of the mod file in KB (or MB)
 * @field version: Mod version from metadata
 */
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class Mod {

    private String fileName;
    private String name;
    private String fileSize;
    private String version;
}
