package com.example.api_servers_nightfall_is_a_dev.Metrics;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MetricsController {

    @Autowired
    private final MetricsService metricsService;

}
