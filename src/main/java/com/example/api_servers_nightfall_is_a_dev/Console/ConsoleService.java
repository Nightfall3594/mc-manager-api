package com.example.api_servers_nightfall_is_a_dev.Console;

import com.example.api_servers_nightfall_is_a_dev.common.ServerStatus;
import com.example.api_servers_nightfall_is_a_dev.common.rcon.RconClientService;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.glavo.rcon.Rcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
@AllArgsConstructor
public class ConsoleService {

    @Autowired
    private final KubernetesClient kubernetesClient;

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private final RconClientService rconClientService;

    @Autowired
    private final ServerStatus serverStatus;

    @Async
    public void getConsoleLog() {

        if(!serverStatus.isOnline()) {
            simpMessagingTemplate.convertAndSend("/topic/console/live",
                    "Server is offline. Please turn on the server and try again");
            return;
        }

        kubernetesClient.pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .watchLog(new StompLogOutputAdapter("/topic/console/live", simpMessagingTemplate));
    }

    public void runCommand(String command) {
        try {
            Rcon rconClient = rconClientService.getRconClient();

            if (rconClient == null) {
                simpMessagingTemplate.convertAndSend("/topic/console/live",
                        "Server is off. Please turn on the server and try again");
            } else {
                // note: trim to remove occasional crlf characters
                String output = rconClient.command(command.trim());
                if(!output.isEmpty()) {
                    simpMessagingTemplate.convertAndSend("/topic/console/live", output);
                }
            }

        } catch (IOException e) {
            simpMessagingTemplate.convertAndSend("/topic/console/live",
                    "Server is unreachable. Please check your internet connection or try again");
        }
    }
}
