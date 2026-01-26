package com.example.api_servers_nightfall_is_a_dev.Backups;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class BackupService {
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
}