package ru.yandex.practicum.stats.server.stats;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.stats.dto.EndpointHit;
import ru.yandex.practicum.stats.dto.ViewStats;
import ru.yandex.practicum.stats.server.mapper.StatsMapper;
import ru.yandex.practicum.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceStats implements ServiceStatsInt {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsMapper statsMapper;
    private final StatsRepository statsRepository;

    @Override
    public EndpointHit saveHit(EndpointHit hit) {
        Hit hit1 = statsMapper.toHit(hit);
        return statsMapper.toDto(statsRepository.save(hit1));
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {
        if (end == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"EndDate cannot be null");
        }
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);
        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,"Start must be before end");
        }
        if (unique) {
            return statsRepository.getStatsUnique(startDate, endDate, uris);
        }
        return statsRepository.getStats(startDate, endDate, uris);
    }
}
