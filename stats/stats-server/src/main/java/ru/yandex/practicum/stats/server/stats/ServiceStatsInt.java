package ru.yandex.practicum.stats.server.stats;

import ru.yandex.practicum.stats.dto.EndpointHit;
import ru.yandex.practicum.stats.dto.ViewStats;

import java.util.List;

public interface ServiceStatsInt {
    EndpointHit saveHit(EndpointHit hit);

    List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique);
}
