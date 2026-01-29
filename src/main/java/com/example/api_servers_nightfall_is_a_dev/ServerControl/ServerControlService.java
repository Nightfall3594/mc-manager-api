package com.example.api_servers_nightfall_is_a_dev.ServerControl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ServerControlService {

    public void startServer() {
        // TODO: Logic to start server.
        //  This will involve kubernetes REST API calls to scale deployments
    }

    public void stopServer() {
        // TODO: Logic to stop server
        //  This will involve kubernetes REST API calls to scale deployments
    }
}
