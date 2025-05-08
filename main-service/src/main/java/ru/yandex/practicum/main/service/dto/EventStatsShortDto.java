package ru.yandex.practicum.main.service.dto;

import lombok.Data;

@Data
public class EventStatsShortDto {
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private Integer id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
    private Integer dislikes;
    private Integer likes;
}
