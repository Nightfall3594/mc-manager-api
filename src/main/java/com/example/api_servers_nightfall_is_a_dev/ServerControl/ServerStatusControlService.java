package com.example.api_servers_nightfall_is_a_dev.ServerControl;

import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ServerStatusControlService {

    @Autowired
    private final KubernetesClient kubernetesClient;

    public void startServer() {
        kubernetesClient.apps()
                .statefulSets()
                .inNamespace("chillingmc")
                .withName("chillingmc")
                .scale(1);
    }

    public void stopServer() {
        kubernetesClient.apps()
                .statefulSets()
                .inNamespace("chillingmc")
                .withName("chillingmc")
                .scale(0);
    }
}
