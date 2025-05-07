package ru.yandex.practicum.main.service.core.service.request;

import ru.yandex.practicum.main.service.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestServiceInt {
    ParticipationRequestDto createRequest(Integer userId, Integer eventId);

    ParticipationRequestDto cancelParticipation(Integer userId, Integer requestId);

    List<ParticipationRequestDto> getUserRequests(Integer userId);
}
