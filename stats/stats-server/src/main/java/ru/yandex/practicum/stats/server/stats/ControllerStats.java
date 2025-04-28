package ru.yandex.practicum.stats.server.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.stats.dto.EndpointHit;
import ru.yandex.practicum.stats.dto.ViewStats;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ControllerStats {
    private final ServiceStatsInt serviceStatsInt;

    @PostMapping("/hit")
    public EndpointHit saveHit(@RequestBody EndpointHit hit) {
        return serviceStatsInt.saveHit(hit);
    }

    @GetMapping("/ru/yandex/practicum/stats/server/stats")
    public List<ViewStats> getStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        return serviceStatsInt.getStats(start, end, uris, unique);
    }
}
