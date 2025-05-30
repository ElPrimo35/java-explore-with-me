package ru.yandex.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
