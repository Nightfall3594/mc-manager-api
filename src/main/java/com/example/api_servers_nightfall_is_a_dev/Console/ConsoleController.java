package com.example.api_servers_nightfall_is_a_dev.Console;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
public class ConsoleController {

    @Autowired
    private final ConsoleService consoleService;

    @SendToUser("/topic/console/live")
    @SubscribeMapping("/topic/console/live")
    public String getLogs() {
        return consoleService.getConsoleLog();
    }

    @MessageMapping("/topic/console/command")
    public void executeCommand(String command) {
        consoleService.runCommand(command);
    }
}
