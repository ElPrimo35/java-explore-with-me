package ru.yandex.practicum.stats.server.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.stats.dto.EndpointHit;
import ru.yandex.practicum.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatsMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Hit toHit(EndpointHit hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setIp(hitDto.getIp());
        hit.setUri(hitDto.getUri());
        hit.setTimestamp(LocalDateTime.parse(hitDto.getTimestamp(), formatter));
        return hit;
    }

    public EndpointHit toDto(Hit hit) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setId(hit.getId());
        endpointHit.setApp(hit.getApp());
        endpointHit.setIp(hit.getIp());
        endpointHit.setUri(hit.getUri());
        endpointHit.setTimestamp(hit.getTimestamp().format(formatter));
        return endpointHit;
    }
}
