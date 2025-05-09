package ru.yandex.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@EqualsAndHashCode
@AllArgsConstructor
public class LikeId implements Serializable {
    private Long userId;
    private Long eventId;
}
