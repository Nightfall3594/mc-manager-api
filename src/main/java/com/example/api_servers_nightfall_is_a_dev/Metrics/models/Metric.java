package com.example.api_servers_nightfall_is_a_dev.Metrics.models;

import lombok.*;

import java.math.BigInteger;

@Data
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Metric {

    private boolean isOnline;
    private int tps;
    private BigInteger uptime;

    private double cpu;
    private BigInteger ram;
    private BigInteger disk;

    private double maxCpu;
    private BigInteger maxRam;
    private BigInteger maxDisk;

    private int players;
    private int maxPlayers;

}
