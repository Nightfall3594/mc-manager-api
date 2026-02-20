package com.example.api_servers_nightfall_is_a_dev.Backups;

import com.example.api_servers_nightfall_is_a_dev.common.GlobalCache;
import com.example.api_servers_nightfall_is_a_dev.common.rcon.RconClientService;
import lombok.RequiredArgsConstructor;
import org.glavo.rcon.Rcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class BackupService {

    @Autowired
    RconClientService rconClientService;

    @Autowired
    GlobalCache globalCache;

    public void getBackup(OutputStream output) {

        final Path BACKUP_PATH = Paths.get("data/");

        try (ZipOutputStream zos = new ZipOutputStream(output);
             Stream<Path> pathStream = Files.walk(BACKUP_PATH)) {

            pathStream.filter(Files::isRegularFile)
                    .forEach(path -> {

                        // Skip the session lock file (JVM locked)
                        if (path.getFileName().toString().equals("session.lock")) { return; }

                        try {
                            zos.putNextEntry(new ZipEntry(
                                BACKUP_PATH
                                    .relativize(path)
                                    .toString()

                                    // for cross-os compatibility
                                    .replace("\\", "/")
                                ));

                            Files.copy(path, zos);
                            zos.closeEntry();
                        } catch (IOException e) { e.printStackTrace(); }
                    });
        } catch (IOException e) { e.printStackTrace(); }
    }

    public BackupMetadata getBackupMetadata() {
        return new BackupMetadata(
                getFileSize(),
                getLastUpdated(),
                getWorldSeed()
        );
    }

    /**
     * Get the world's file size
     * @return file size of the world in bytes
     */
    private BigInteger getFileSize() {

        String worldFolder = String.format("data/minecraft/%s", getWorldName());
        Path worldFolderPath = Paths.get(worldFolder);

        try (Stream<Path> worldContents = Files.walk(worldFolderPath)) {

            long worldSize = worldContents.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .filter(path -> !path.getFileName().toString().equals("session.lock"))
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .sum();

            return BigInteger.valueOf(worldSize);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the last update time of the world
     * @return last updated date time in ISO format
     */
    private String getLastUpdated() {
        String worldFolder = String.format("data/minecraft/%s", getWorldName());
        Path worldFolderPath = Paths.get(worldFolder);
        try {
            return Files.getLastModifiedTime(worldFolderPath)
                    .toInstant()
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getWorldSeed() {

        String worldSeed = globalCache.get("world_seed");

        if(worldSeed == null) {
            Rcon rconClient = rconClientService.getRconClient();
            if(rconClient == null) { throw new RuntimeException("RCON is inaccessible"); }
            try {
                String input = rconClient.command("seed");
                Pattern pattern = Pattern.compile("Seed: \\[(.*?)]");
                Matcher matcher = pattern.matcher(input);

                if (matcher.find()) {
                    String seed = matcher.group(1);
                    globalCache.set("world_seed", seed);
                    return seed;
                }

                return "";

            } catch (IOException e) { e.printStackTrace(); }
        }
        return worldSeed;
    }

    private String getWorldName() {

        final Path serverPropertiesFile = Paths.get("data/minecraft/server.properties");
        try {
            String worldNameProperty = Files.readAllLines(serverPropertiesFile)
                    .stream()
                    .filter(line -> line.startsWith("level-name="))
                    .findFirst()
                    .orElse("");

            if(worldNameProperty.isEmpty()) throw new RuntimeException("Invalid world name");

            return worldNameProperty.split("=")[1];

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}