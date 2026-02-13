package com.example.api_servers_nightfall_is_a_dev.common;

import org.glavo.rcon.AuthenticationException;
import org.glavo.rcon.Rcon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

@Configuration
public class RconConfig {

    @Bean
    @Lazy
    public Rcon rconClient() {
        String rconHost = System.getenv("RCON_HOST");
        String rconPort = System.getenv("RCON_PORT");
        String rconPassword = System.getenv("RCON_PASSWORD");

        if(rconHost.isEmpty() || rconPort.isEmpty() || rconPassword.isEmpty()){
            throw new IllegalStateException("RCON_HOST or RCON_PASSWORD is not set");
        }

        try {
            return new Rcon(
                    rconHost,
                    Integer.parseInt(rconPort),
                    rconPassword
            );
        } catch (AuthenticationException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
