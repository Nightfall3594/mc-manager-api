package com.example.api_servers_nightfall_is_a_dev.Backups;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Controller
@RequiredArgsConstructor
public class BackupController {

    @Autowired
    private final BackupService backupService;

    @GetMapping("/world/backup")
    public ResponseEntity<StreamingResponseBody> getBackup(){

        StreamingResponseBody body = backupService::getBackup;
        return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"backup.zip\"")
                    .header("Content-Type", "application/zip")
                    .body(body);
    }
}