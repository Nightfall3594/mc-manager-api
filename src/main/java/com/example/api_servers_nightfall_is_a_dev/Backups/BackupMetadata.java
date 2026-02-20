package com.example.api_servers_nightfall_is_a_dev.Backups;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class BackupMetadata {
    BigInteger fileSize;
    String lastModified;
    String worldSeed;
}
