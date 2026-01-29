package com.example.api_servers_nightfall_is_a_dev.ServerControl;

import com.example.api_servers_nightfall_is_a_dev.common.ServerStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ServerController {

    @Autowired
    private final ServerControlService serverControlService;

    @Autowired
    private final ServerStatus serverStatus;

    @PostMapping("/server/start")
    public void startServer() {

        if(!serverStatus.isOnline()) {
            throw new IllegalStateException("Server is already running.");
        }

        serverControlService.startServer();
    }

    @PostMapping("/server/stop")
    public void stopServer() {

        if(serverStatus.isOnline()) {
            throw new IllegalStateException("Server is not running.");
        }

        serverControlService.stopServer();
    }

    @PostMapping("/server/restart")
    public void restartServer() {

        if(!serverStatus.isOnline()) {
            throw new IllegalStateException("Server is not running.");
        }

        serverControlService.stopServer();
        serverControlService.startServer();
    }
}
