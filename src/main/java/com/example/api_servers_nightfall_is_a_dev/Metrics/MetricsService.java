package com.example.api_servers_nightfall_is_a_dev.Metrics;

import com.example.api_servers_nightfall_is_a_dev.common.ServerStatus;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;

@Service
@EnableScheduling
@AllArgsConstructor
public class MetricsService {

    @Autowired
    private final SimpMessagingTemplate template;

    @Autowired
    private final ServerStatus serverStatus;

    // Temporary random generator for mock data
    private final Random randomGenerator = new Random();

    @Scheduled(fixedRate = 1000)
    public void pollData(){
        // TODO: Add ways to gather metrics
        Metric metric = Metric.builder()
                .isOnline(serverStatus.isOnline())
                .tps(getTPS())
                .uptime(getUptime())
                .cpu(getCpuUsage())
                .ram(getRAMUsage())
                .disk(getDiskUsage())
                .players(getPlayerCount())
                .build();

        template.convertAndSend("/topic/metrics/live", metric);
    }


    // TODO: Implement actual data gathering methods

    /**
     * Get current ticks per second.
     * @return ticks per second (0-20)
     */
    private int getTPS(){
        return randomGenerator.nextInt(0, 20);
    }

    private LocalDate getUptime(){
        return null;
    }

    /**
     * Get total cpu usage.
     * @return percentage of cpu usage (0-100)
     */
    private float getCpuUsage() {
        return randomGenerator.nextFloat(0, 100);
    }

    /**
     * Get total ram usage.
     * @return percentage of ram usage (0-100)
     */
    private float getRAMUsage() {
        return randomGenerator.nextFloat(0, 100);
    }

    /**
     * Get total disk usage.
     * @return percentage of disk usage (0-100)
     */
    private float getDiskUsage() {
        return randomGenerator.nextFloat(0, 100);
    }

    /**
     * Get current player count.
     * @return number of players online
     */
    private int getPlayerCount(){
        return randomGenerator.nextInt(0,10);
    }
}
