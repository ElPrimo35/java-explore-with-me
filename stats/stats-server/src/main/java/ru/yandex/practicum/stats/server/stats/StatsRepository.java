package ru.yandex.practicum.stats.server.stats;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.stats.dto.ViewStats;
import ru.yandex.practicum.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Integer> {

    @Query("select new ru.yandex.practicum.stats.dto.ViewStats(h.app, h.uri, COUNT(h.ip)) " +
            "from Hit h " +
            "where h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (?3 IS NULL OR h.uri IN ?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);


    @Query("select new ru.yandex.practicum.stats.dto.ViewStats(h.app, h.uri, COUNT(DISTINCT h.ip)) " +
            "from Hit h " +
            "where h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (?3 IS NULL OR h.uri IN ?3) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC")
    List<ViewStats> getStatsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

}
