package com.example.api_servers_nightfall_is_a_dev.Metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

//GET    /metrics/server/cpu
//GET    /metrics/server/ram
//GET    /metrics/server/disk

//GET    /metrics/status  # online/offline
//GET    /metrics/players
//GET    /metrics/uptime
//GET    /metrics/tps

@Controller
public class MetricsController {

    @Autowired
    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService){
        this.metricsService = metricsService;
    }

    @SubscribeMapping("/metrics/live")
    @SendTo("/metrics")
    public void getMetrics(){
        // TODO: handlers upon subscription to /metrics broker
    }

}
