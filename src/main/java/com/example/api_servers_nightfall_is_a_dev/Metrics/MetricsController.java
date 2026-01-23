package com.example.api_servers_nightfall_is_a_dev.Metrics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MetricsController {

    @Autowired
    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService){
        this.metricsService = metricsService;
    }
}
