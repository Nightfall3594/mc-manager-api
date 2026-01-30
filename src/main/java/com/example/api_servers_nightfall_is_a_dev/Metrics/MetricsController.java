package com.example.api_servers_nightfall_is_a_dev.Metrics;

import com.example.api_servers_nightfall_is_a_dev.Metrics.models.Event;
import com.example.api_servers_nightfall_is_a_dev.Metrics.models.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MetricsController {

    @Autowired
    private final MetricsService metricsService;

    @GetMapping("/metrics/events")
    public List<Event> getEvents(){
        return metricsService.getEvents();
    }

    @GetMapping("/metrics/players")
    public List<Player> getActivePlayers(){
        return metricsService.getOnlinePlayers();
    }
}
