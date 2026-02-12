package com.example.api_servers_nightfall_is_a_dev.Metrics;

import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class K8SClientConfiguration {

    @Bean
    public KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder()
                .withConfig(new ConfigBuilder()
                        .withMasterUrl(System.getenv("K8S_URL"))
                        .withOauthToken(System.getenv("K8S_TOKEN"))
                        .withTrustCerts(true)
                        .build()
                )
                .build();
    }
}
