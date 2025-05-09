package ru.yandex.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.main.service.model.Event;

@Data
@AllArgsConstructor
public class EventStatsDto {
    private Event event;
    private Long likes;
    private Long dislikes;
}
