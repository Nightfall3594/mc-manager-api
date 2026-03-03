package com.example.api_servers_nightfall_is_a_dev.Backups;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class BackupMetadata {
    String worldName;
    BigInteger fileSize;
    String lastModified;
    String worldSeed;
}
