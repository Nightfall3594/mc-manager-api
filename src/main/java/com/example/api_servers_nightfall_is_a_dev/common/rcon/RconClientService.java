package com.example.api_servers_nightfall_is_a_dev.common.rcon;

import org.glavo.rcon.AuthenticationException;
import org.glavo.rcon.Rcon;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * A small service wrapper for the RCON client.
 * This class manages the lifecycle of RCON clients,
 * including handling connections, disconnections, and catching errors.
 */
public class RconClientService {

    private Rcon rconClient;
    private final String host;
    private final int port;
    private final String password;

    public RconClientService(String host, int port, String password) {
        this.host = host;
        this.port = port;
        this.password = password;
    }

    public Rcon getRconClient() {

        if (rconClient == null) {
            try {
                rconClient = new Rcon(host, port, password);
                return rconClient;

            } catch (IOException | NegativeArraySizeException e ) {
                closeRconClient();
                return null;
            } catch (AuthenticationException e) {
                throw new RuntimeException(e);
            }

        } else {
            return rconClient;
        }
    }

    public void closeRconClient() {
        try {
            if(rconClient != null) {
                rconClient.close();
                rconClient = null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
