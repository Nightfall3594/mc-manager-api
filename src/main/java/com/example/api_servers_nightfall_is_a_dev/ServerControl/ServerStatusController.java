package com.example.api_servers_nightfall_is_a_dev.ServerControl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ServerStatusController {

    @Autowired
    private final ServerStatusControlService serverStatusControlService;

    @PutMapping("/server/status")
    public ResponseEntity<String> setServerStatus(@RequestBody Map<String, Boolean> body) {

        boolean shouldBeOnline = body.get("isOnline");

        if (shouldBeOnline) {
            serverStatusControlService.startServer();
            return ResponseEntity.ok().body("Server is online");
        } else {
            serverStatusControlService.stopServer();
            return ResponseEntity.ok().body("Server is offline");
        }
    }
}