package com.example.api_servers_nightfall_is_a_dev.Metrics;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@NoArgsConstructor
public class MetricsService {

    @Autowired
    private SimpMessagingTemplate template;

    private MetricsService(SimpMessagingTemplate template){
        this.template = template;
    }

    @Scheduled(fixedRate = 1000)
    public void pollData(){
        // TODO: Add ways to gather metrics
        Metric metric = new Metric();
        template.convertAndSend("/metrics", metric);
    }

}
