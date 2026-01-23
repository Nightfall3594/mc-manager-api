package com.example.api_servers_nightfall_is_a_dev.Metrics;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Metric {

    private boolean isOnline;
    private int tps;
    private LocalDate uptime;

    private float cpu;
    private float ram;
    private float disk;

    private int players;

}
