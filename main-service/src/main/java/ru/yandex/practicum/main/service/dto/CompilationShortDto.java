package ru.yandex.practicum.main.service.dto;

import lombok.Data;

@Data
public class CompilationShortDto {
    private Integer id;
    private Boolean pinned;
    private String title;
}
