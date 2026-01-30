package com.example.api_servers_nightfall_is_a_dev.Metrics.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class Event {
    private String timestamp;
    private String message;

    public Event(String timestamp, String message) {
        this.timestamp = timestamp;
        this.message = message;
    }
}
