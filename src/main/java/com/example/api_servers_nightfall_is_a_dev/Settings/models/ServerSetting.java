package com.example.api_servers_nightfall_is_a_dev.Settings.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ServerSetting {
    private String key;
    private String value;
    private FieldType type;
    private List<String> options;
}
