package ru.yandex.practicum.main.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompilationDto {
    private List<EventShortDto> events;
    private Integer id;
    private Boolean pinned;
    private String title;
}
