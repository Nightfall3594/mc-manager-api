package com.example.api_servers_nightfall_is_a_dev.Console;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ConsoleController {

    private final ConsoleService consoleService;

    @MessageMapping("/topic/console/command")
    public void executeCommand(String command) {
        consoleService.runCommand(command);
    }
}
