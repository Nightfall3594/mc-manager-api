package com.example.api_servers_nightfall_is_a_dev.Settings;

import com.example.api_servers_nightfall_is_a_dev.Settings.models.ServerSetting;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class ServerSettingsController {

    @Autowired
    private final ServerSettingService settingService;

    @GetMapping("/server-properties")
    public List<ServerSetting> getGamerules() {
        return settingService.getServerSetting();
    }

    @PostMapping("/server-properties")
    public ResponseEntity<String> updateGamerules(@RequestBody Map<String, String> newGamerules) {
        settingService.updateServerSetting(newGamerules);
        return ResponseEntity.ok()
                .body("Gamerules updated successfully");
    }
}