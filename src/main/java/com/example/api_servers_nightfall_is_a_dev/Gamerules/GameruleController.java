package com.example.api_servers_nightfall_is_a_dev.Gamerules;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class GameruleController {

    @Autowired
    private final GameruleService gameruleService;

    @GetMapping("/gamerules")
    public Map<String, String> getGamerules() {
        return gameruleService.getGamerules();
    }
}