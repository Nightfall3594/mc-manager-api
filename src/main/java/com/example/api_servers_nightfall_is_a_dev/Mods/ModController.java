package com.example.api_servers_nightfall_is_a_dev.Mods;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class ModController {

    @Autowired
    private final ModService modService;

    @GetMapping("/mods")
    public List<Mod> listMods() {
        return modService.listMods();
    }

    @GetMapping("/mods/download")
    public ResponseEntity<byte[]> downloadMod(@RequestParam String fileName) {

        byte[] modData = modService.getModFile(fileName);
        if (modData == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .header("Content-Type", "application/java-archive")
                .body(modData);
    }

    @DeleteMapping("/mods")
    public ResponseEntity<String> deleteMod(@RequestParam String fileName) {
        boolean deleted = modService.deleteModFile(fileName);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/mods")
    public ResponseEntity<String> uploadMod( @RequestParam("file") MultipartFile file) throws IOException {
        modService.saveModFile(file.getOriginalFilename(), file.getBytes());
        return ResponseEntity.ok().build();
    }

}