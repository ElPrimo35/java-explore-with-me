package ru.yandex.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
@AllArgsConstructor
public class DislikeId implements Serializable {
    private Long userId;
    private Long eventId;
}
