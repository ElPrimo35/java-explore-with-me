package ru.yandex.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}
