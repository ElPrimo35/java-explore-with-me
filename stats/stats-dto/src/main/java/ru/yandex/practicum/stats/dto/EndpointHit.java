package ru.yandex.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class EndpointHit {
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
