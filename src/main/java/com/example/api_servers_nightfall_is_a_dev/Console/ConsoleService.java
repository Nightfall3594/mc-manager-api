package com.example.api_servers_nightfall_is_a_dev.Console;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class ConsoleService {

    @Autowired
    private final SimpMessagingTemplate template;

    @Scheduled(fixedRate = 1000)
    public void sendConsoleLog() {
        // TODO: poll latest.log, read latest line, and send
        String logMessage = "Sample log message";
        System.out.println("Sent log: " + logMessage);
        template.convertAndSend("/topic/console/live", logMessage);
    }

    public void runCommand(String command) {
        // TODO: Run RCON command
        System.out.println("Executed command: " + command);
        template.convertAndSend("/topic/console/live", command);
    }

}
