package ru.yandex.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.main.service.dto.ParticipationRequestDto;
import ru.yandex.practicum.main.service.dto.Status;
import ru.yandex.practicum.main.service.model.Event;
import ru.yandex.practicum.main.service.model.Request;
import ru.yandex.practicum.main.service.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RequestMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto toDto(Integer userId, Integer eventId, LocalDateTime created, Status status) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setCreated(created.format(formatter));
        requestDto.setEvent(eventId);
        requestDto.setRequester(userId);
        requestDto.setStatus(String.valueOf(status));
        return requestDto;
    }

    public ParticipationRequestDto toDto(Request request, Status status) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated().format(formatter));
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(String.valueOf(status));
        return requestDto;
    }

    public ParticipationRequestDto toDto(Request request) {
        ParticipationRequestDto requestDto = new ParticipationRequestDto();
        requestDto.setId(request.getId());
        requestDto.setCreated(request.getCreated().format(formatter));
        requestDto.setEvent(request.getEvent().getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(String.valueOf(request.getStatus()));
        return requestDto;
    }

    public Request toRequest(ParticipationRequestDto requestDto, User requester, Event event) {
        Request request = new Request();
        request.setCreated(LocalDateTime.parse(requestDto.getCreated(), formatter));
        request.setRequester(requester);
        request.setEvent(event);
        request.setStatus(Status.valueOf(requestDto.getStatus()));
        return request;
    }
}
