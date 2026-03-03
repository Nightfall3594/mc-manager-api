package com.example.api_servers_nightfall_is_a_dev.Console;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class ConsoleLogConfig {

    @Autowired
    private KubernetesClient kubernetesClient;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Bean
    public LogWatch consoleLogWatch(KubernetesClient kubernetesClient) {
        return kubernetesClient.pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .watchLog(new StompLogOutputAdapter("/topic/console/live", simpMessagingTemplate));
    }
}
