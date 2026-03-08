package com.example.api_servers_nightfall_is_a_dev.Mods;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.moandjiezana.toml.Toml;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

@Service
public class ModService {

    public List<Mod> listMods() {
       Path modsFolder = Path.of("data/minecraft/mods/")
               .toAbsolutePath()
               .normalize();

       try(Stream<Path> files = Files.list(modsFolder)) {
           return files
                   .filter(Files::isRegularFile)
                   .map(file -> {
                       return Mod.builder()
                                 .fileName(getFilename(file))
                                 .name(getModName(file))
                                 .fileSize(getModFileSize(file))
                                 .version(getModVersion(file))
                                 .build();
                   }).toList();

       } catch (Exception e) {
           throw new RuntimeException("Error listing mods in folder: " + modsFolder, e);
       }

    }

    private String getFilename(Path modFile) {
        return URLEncoder.encode(modFile.getFileName().toString(), StandardCharsets.UTF_8);
    }

    private String getModName(Path modFile) {

        try(JarFile jarFile = new JarFile(modFile.toFile())) {

            // For fabric mod metadata
            JarEntry fabricJarEntry = jarFile.getJarEntry("fabric.mod.json");
            if(fabricJarEntry != null) {
                InputStream jis = jarFile.getInputStream(fabricJarEntry);
                Gson jsonParser = new Gson();
                JsonObject modJson = jsonParser.fromJson(new InputStreamReader(jis), JsonObject.class);

                if(modJson.has("name")) return modJson.get("name").getAsString();
            }

            // For forge mod metadata
            JarEntry forgeJarEntry = jarFile.getJarEntry("META-INF/mods.toml");
            if(forgeJarEntry != null) {
                InputStream jis = jarFile.getInputStream(forgeJarEntry);

                Toml tomlFile = new Toml().read(jis);
                List<Toml> modsList = tomlFile.getTables("mods");

                // note: forge mod metadata has multiple mods
                //  only the first item contains the actual metadata
                if (modsList != null && !modsList.isEmpty()) {
                    Toml firstMod = modsList.getFirst();
                    if (firstMod.contains("displayName")) {
                        return firstMod.getString("displayName");
                    }
                }
            }

            return getFilename(modFile);

        } catch (IOException e) {
            return getFilename(modFile);
        }
    }

    private String getModVersion(Path modFile) {
        try(JarFile jarFile = new JarFile(modFile.toFile())) {

            // For fabric mod metadata
            JarEntry fabricJarEntry = jarFile.getJarEntry("fabric.mod.json");
            if(fabricJarEntry != null) {
                InputStream jis = jarFile.getInputStream(fabricJarEntry);
                Gson jsonParser = new Gson();
                JsonObject modJson = jsonParser.fromJson(new InputStreamReader(jis), JsonObject.class);

                if(modJson.has("version")) return modJson.get("version").getAsString();
            }

            // For forge mod metadata
            JarEntry forgeJarEntry = jarFile.getJarEntry("META-INF/mods.toml");
            if(forgeJarEntry != null) {
                InputStream jis = jarFile.getInputStream(forgeJarEntry);

                Toml tomlFile = new Toml().read(jis);
                List<Toml> modsList = tomlFile.getTables("version");

                // note: forge mod metadata has multiple mods
                //  only the first item contains the actual metadata
                if (modsList != null && !modsList.isEmpty()) {
                    Toml firstMod = modsList.getFirst();
                    if (firstMod.contains("version")) {
                        return firstMod.getString("version");
                    }
                }
            }

            return "unknown";

        } catch (IOException e) {
            return "unknown";
        }
    }

    /**
     * Gets the size of the mod file in bytes
     * @param modFile mod filename
     * @return size of the mod in bytes
     */
    private BigInteger getModFileSize(Path modFile) {
        try {
            long sizeInBytes = Files.size(modFile);
            return BigInteger.valueOf(sizeInBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error getting file size for mod: " + modFile, e);
        }
    }


    public byte[] getModFile(String fileName) {

        Path modsFolder = Path.of("data/minecraft/mods/").normalize();

        Path modFilePath = modsFolder.resolve(fileName).normalize();

        if (!modFilePath.startsWith(modsFolder)) {
            throw new RuntimeException("Invalid file path");
        }

        if (!Files.exists(modFilePath) || !Files.isRegularFile(modFilePath)) {
            return null;
        }

        try {
            return Files.readAllBytes(modFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Error reading mod file: " + modFilePath, e);
        }
    }

    public boolean deleteModFile(String fileName) {
        Path modsFolder = Path.of("data/minecraft/mods/").normalize();

        Path modFilePath = modsFolder.resolve(fileName).normalize();

        if (!modFilePath.startsWith(modsFolder)) {
            throw new RuntimeException("Invalid file path");
        }

        try {
            return Files.deleteIfExists(modFilePath);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting mod file: " + modFilePath, e);
        }
    }

    public void saveModFile(String fileName, byte[] modData) {
        Path modsFolder = Path.of("data/minecraft/mods/").normalize();

        Path modFilePath = modsFolder.resolve(fileName).normalize();

        if (!modFilePath.startsWith(modsFolder)) {
            throw new RuntimeException("Invalid file path");
        }

        if (Files.exists(modFilePath)) {
            throw new RuntimeException("Mod file already exists: " + modFilePath);
        }

        try {
            Files.write(modFilePath, modData);
        } catch (IOException e) {
            throw new RuntimeException("Error saving mod file: " + modFilePath, e);
        }
    }

}