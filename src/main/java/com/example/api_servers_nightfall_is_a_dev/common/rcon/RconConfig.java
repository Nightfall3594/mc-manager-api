package com.example.api_servers_nightfall_is_a_dev.common.rcon;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class RconConfig {

    @Bean
    public RconClientService rconClient() {
        String rconHost = System.getenv("RCON_HOST");
        String rconPort = System.getenv("RCON_PORT");
        String rconPassword = System.getenv("RCON_PASSWORD");

        if(rconHost.isEmpty() || rconPort.isEmpty() || rconPassword.isEmpty()){
            throw new IllegalStateException("RCON_HOST or RCON_PASSWORD is not set");
        }

        return new RconClientService(rconHost, Integer.parseInt(rconPort), rconPassword);
    }
}
