package ru.yandex.practicum.main.service.dto;

import lombok.Data;

@Data
public class ParticipationRequestDto {
    private Integer id;
    private String created;
    private Integer event;
    private Integer requester;
    private String status;
}
