package com.example.api_servers_nightfall_is_a_dev.common;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Utility class to check server status, and other related stuff.
 * When other services require specific server statuses,
 * migrate those methods here.
 */
@Component
@AllArgsConstructor
public class ServerStatus {

    public boolean isOnline(){
        // TODO: Implement actual server status check logic
        return false;
    }
}
