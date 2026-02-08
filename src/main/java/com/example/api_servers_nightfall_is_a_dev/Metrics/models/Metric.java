package com.example.api_servers_nightfall_is_a_dev.Metrics.models;

import lombok.*;

import java.math.BigInteger;
import java.time.LocalDate;

@Data
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Metric {

    private boolean isOnline;
    private int tps;
    private LocalDate uptime;

    private double cpu;
    private BigInteger ram;
    private float disk;

    private double maxCpu;
    private BigInteger maxRam;
    private float maxDisk;

    private int players;

}
