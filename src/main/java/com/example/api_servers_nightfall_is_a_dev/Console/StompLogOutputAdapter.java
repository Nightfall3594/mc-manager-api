package com.example.api_servers_nightfall_is_a_dev.Console;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@AllArgsConstructor
public class StompLogOutputAdapter extends OutputStream {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final String logUrl;

    private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    public StompLogOutputAdapter(String logUrl, SimpMessagingTemplate simpMessagingTemplate) {
           this.simpMessagingTemplate = simpMessagingTemplate;
           this.logUrl = logUrl;
    }

    @Override
    public void write(int b) {
        if(b == '\n' && byteArrayOutputStream.size() > 0) {
            simpMessagingTemplate.convertAndSend(logUrl,
                    byteArrayOutputStream.toString(StandardCharsets.UTF_8));
            byteArrayOutputStream.reset();
        } else if (b != '\r') {
            byteArrayOutputStream.write(b);
        }
    }
}
