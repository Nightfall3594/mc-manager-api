package com.example.api_servers_nightfall_is_a_dev.Backups;

import com.example.api_servers_nightfall_is_a_dev.common.ServerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequiredArgsConstructor
public class BackupController {

    @Autowired
    private final BackupService backupService;

    @Autowired
    private final ServerStatus serverStatus;

    @GetMapping("/world/backup")
    public ResponseEntity<StreamingResponseBody> getBackup(){

        if (!serverStatus.isOnline()) {
            return ResponseEntity
                    .badRequest()
                    .body(null);
        }

        StreamingResponseBody body = backupService::getBackup;
        return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"backup.zip\"")
                    .header("Content-Type", "application/zip")
                    .body(body);
    }

    @GetMapping("/world/backup/data")
    public BackupMetadata getBackupMetadata() {
        return backupService.getBackupMetadata();
    }
}