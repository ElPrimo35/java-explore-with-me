package client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.stats.dto.EndpointHit;
import ru.yandex.practicum.stats.dto.ViewStats;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class StatsClient {
    private final RestClient restClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Value("${server.base.url}")
    private final String serverUrl;

    @Autowired
    public StatsClient(String serverUrl) {
        this.serverUrl = serverUrl;
        this.restClient = RestClient.builder()
                .baseUrl(serverUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public void saveHit(EndpointHit hit) {
        restClient.post()
                .uri("/hit")
                .body(hit)
                .retrieve()
                .toBodilessEntity();
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        URI uri = UriComponentsBuilder.fromHttpUrl(serverUrl)
                .path("/stats")
                .queryParam("start", encodeDateTime(start))
                .queryParam("end", encodeDateTime(end))
                .queryParamIfPresent("uris", uris != null && !uris.isEmpty() ?
                        Optional.of(String.join(",", uris)) : Optional.empty())
                .queryParam("unique", unique)
                .build()
                .toUri();

        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<List<ViewStats>>() {
                });
    }

    private String encodeDateTime(LocalDateTime dateTime) {
        return dateTime.format(formatter);
    }
}
