package com.example.api_servers_nightfall_is_a_dev.Metrics;

import com.example.api_servers_nightfall_is_a_dev.Metrics.models.Event;
import com.example.api_servers_nightfall_is_a_dev.Metrics.models.Metric;
import com.example.api_servers_nightfall_is_a_dev.Metrics.models.Player;
import com.example.api_servers_nightfall_is_a_dev.common.ServerStatus;
import io.fabric8.kubernetes.api.model.Node;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaim;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.metrics.v1beta1.PodMetrics;
import io.fabric8.kubernetes.client.KubernetesClient;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.util.*;
import java.util.stream.Stream;

@Service
@EnableScheduling
@AllArgsConstructor
public class MetricsService {

    @Autowired
    private final SimpMessagingTemplate template;

    @Autowired
    private final ServerStatus serverStatus;

    @Autowired
    private final KubernetesClient client;

    // Temporary random generator for mock data
    private final Random randomGenerator = new Random();

    @Scheduled(fixedRate = 1000)
    public void pollData(){
        // TODO: Add ways to gather metrics
        Metric metric = Metric.builder()
                .isOnline(serverStatus.isOnline())
                .tps(getTPS())
                .uptime(getUptime())
                .cpu(getCpuUsage())
                .maxCpu(getCpuCapacity())
                .ram(getRAMUsage())
                .maxRam(getRamCapacity())
                .disk(getDiskUsage())
                .maxDisk(getDiskCapacity())
                .players(getPlayerCount())
                .build();

        template.convertAndSend("/topic/metrics/live", metric);
    }


    // TODO: Implement actual data gathering methods

    /**
     * Get current ticks per second.
     * @return ticks per second (0-20)
     */
    private int getTPS(){
        return randomGenerator.nextInt(0, 20);
    }


    /**
     * Get the server's uptime
     * @return total server uptime in seconds
     */
    private BigInteger getUptime(){

        if(!serverStatus.isOnline()){
            return BigInteger.ZERO;
        }

        Pod pod = client.pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .get();

        OffsetDateTime containerStarted = OffsetDateTime.parse(pod.getStatus()
                .getContainerStatuses()
                .getFirst()
                .getState()
                .getRunning()
                .getStartedAt());

        Duration uptime = Duration.between(containerStarted, OffsetDateTime.now());

        return BigInteger.valueOf(uptime.getSeconds());

    }


    /**
     * Get total cpu usage in cores
     * @return cpu usage of the associated pod(s)
     */
    private double getCpuUsage() {

        PodMetrics podMetrics = client.top()
                .pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .metric();

        BigDecimal cpuUsage = podMetrics.getContainers()
                .getFirst()
                .getUsage()
                .get("cpu")
                .getNumericalAmount();

        return cpuUsage.doubleValue();
    }


    /**
     * Maximum cpu capacity of the node in cores
     * @return maximum cpu of the node
     */
    private double getCpuCapacity() {

        Pod pod = client.pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .get();

        String nodeName = pod.getSpec().getNodeName();

        Node node = client.nodes()
                .withName(nodeName)
                .get();

        return node.getStatus()
                .getCapacity()
                .get("cpu")
                .getNumericalAmount()
                .doubleValue();
    }

    /**
     * Get the ram usage of the server container
     * @return ram usage in bytes
     */
    private BigInteger getRAMUsage() {

        PodMetrics metrics = client.top()
                .pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .metric();

        BigDecimal ramUsage = metrics.getContainers()
                .getFirst()
                .getUsage()
                .get("memory")
                .getNumericalAmount();

        return ramUsage.toBigIntegerExact();
    }

    /**
     * Get total ram capacity of the node of the server container
     * @return maximum ram capacity in bytes
     */
    private BigInteger getRamCapacity(){
        Pod pod = client.pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .get();

        String nodeName = pod.getSpec()
                .getNodeName();

        Node node = client.nodes()
                .withName(nodeName)
                .get();

        return node.getStatus()
                .getCapacity()
                .get("memory")
                .getNumericalAmount()
                .toBigIntegerExact();
    }

    /**
     * Get total storage used by the server
     * @return total storage used by the server in bytes
     */
    private BigInteger getDiskUsage() {
        Path folder = Paths.get("data");
        try (Stream<Path> stream = Files.walk(folder)) {
            long size = stream
                    .filter(Files::isRegularFile)
                    .mapToLong(p -> {
                        try {
                            return Files.size(p);
                        } catch (IOException e) {
                            return 0L;
                        }
                    })
                    .sum();

            return BigInteger.valueOf(size);

        } catch (IOException e) {
            return BigInteger.ZERO;
        }
    }

    /**
     * Get the total allocated disk capacity of the server
     * @return maximum disk capacity of the volume/node in bytes
     */
    private BigInteger getDiskCapacity() {

        Pod pod = client.pods()
                .inNamespace("chillingmc")
                .withName("chillingmc-0")
                .get();

        String pvcName = pod.getSpec()
                .getVolumes()
                .getFirst()
                .getPersistentVolumeClaim()
                .getClaimName();

        PersistentVolumeClaim pvc = client.persistentVolumeClaims()
                .inNamespace("chillingmc")
                .withName(pvcName)
                .get();

        return pvc.getStatus()
                    .getCapacity()
                    .get("storage")
                    .getNumericalAmount()
                    .toBigIntegerExact();
    }

    /**
     * Get current player count.
     * @return number of players online
     */
    private int getPlayerCount(){
        return randomGenerator.nextInt(0,10);
    }

    /**
     * Helper method to parse latest.log for join/leave events.
     * @return List of join/leave events
     */
    private List<Event> parseLatestLog() {
        Path logFilePath = Path.of("data/minecraft/logs/latest.log");
        List<Event> events = new ArrayList<>();
        try {
            Files.readAllLines(logFilePath).stream()
                    .filter(log ->
                            log.contains("joined the game")
                            || log.contains("left the game"))
                    .forEach(log -> {
                        String timestamp = log.substring(1, 9);
                        String message = log.split("]:", 2)[1].strip();
                        events.add(new Event(timestamp, message));
                    });

            return events;
        } catch (IOException e) {
            throw new RuntimeException("Unable to read log file: ", e);
        }
    }

    /**
     * Get the last 5 join/leave events from latest.log
     * @return List of last 5 join/leave events
     */
    public List<Event> getEvents(){
        List<Event> events = parseLatestLog();
        if(events.size() <= 5) return events;
        return events.subList(events.size() - 5, events.size());

    }

    /**
     * Get the players that are currently online,
     * along with their playtime.
     * @return List of online players
     */
    public List<Player> getOnlinePlayers(){

        List<Event> playerLogs = parseLatestLog().stream()
                .filter(log ->
                        log.getMessage().contains("joined the game")
                                || log.getMessage().contains("left the game"))
                .toList();

        // List of all active usernames
        Map<String, LocalTime> joinTimes = new HashMap<>();
        playerLogs.forEach(log -> {
            if(log.getMessage().contains("joined the game")) {
                String username = log.getMessage().replace(" joined the game", "").strip();
                LocalTime joinTime = LocalTime.parse(log.getTimestamp());
                joinTimes.put(username, joinTime);

            } else if (log.getMessage().contains("left the game")) {
                String username = log.getMessage().replace(" left the game", "").strip();
                joinTimes.remove(username);
            }
        });

        List<Player> playerList = new ArrayList<>();
        joinTimes.keySet().forEach(playerName -> {
            playerList.add(new Player(playerName, joinTimes.get(playerName).toString()));
        });

        return playerList;
    }

    @PreDestroy
    public void closeK8sClient(){
        client.close();
    }
}
