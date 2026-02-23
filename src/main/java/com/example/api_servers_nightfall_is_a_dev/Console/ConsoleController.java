package com.example.api_servers_nightfall_is_a_dev.Console;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ConsoleController {

    @Autowired
    private final ConsoleService consoleService;

    @SubscribeMapping("/topic/console/live")
    public void getLogs() {
        consoleService.getConsoleLog();
    }

    @MessageMapping("/topic/console/command")
    public void executeCommand(String command) {
        consoleService.runCommand(command);
    }
}
