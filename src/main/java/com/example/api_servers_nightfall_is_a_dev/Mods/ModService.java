package com.example.api_servers_nightfall_is_a_dev.Mods;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
        // TODO: Get mod name from file metadata if possible
        //  File name for now :)
        return modFile.getFileName().toString();
    }

    private String getModVersion(Path modFile) {
        // TODO: Implement version extraction logic based on mod file type
        return "1.0.0";
    }

    private String getModFileSize(Path modFile) {
        try {
            long sizeInBytes = Files.size(modFile);
            return sizeInBytes / 1024 + " KB";
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

    public boolean saveModFile(String fileName, byte[] modData) {
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
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Error saving mod file: " + modFilePath, e);
        }
    }

}