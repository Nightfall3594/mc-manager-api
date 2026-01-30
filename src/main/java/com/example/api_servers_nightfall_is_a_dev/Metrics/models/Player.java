package com.example.api_servers_nightfall_is_a_dev.Metrics.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalTime;

@Data
@AllArgsConstructor
public class Player {
    private String name;
    private String joinTime;
}
