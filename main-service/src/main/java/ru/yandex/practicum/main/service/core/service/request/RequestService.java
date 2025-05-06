package ru.yandex.practicum.main.service.core.service.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.main.service.core.repository.event.EventRepository;
import ru.yandex.practicum.main.service.core.repository.request.RequestRepository;
import ru.yandex.practicum.main.service.core.repository.user.UserRepository;
import ru.yandex.practicum.main.service.dto.ParticipationRequestDto;
import ru.yandex.practicum.main.service.dto.State;
import ru.yandex.practicum.main.service.dto.Status;
import ru.yandex.practicum.main.service.exception.ConflictException;
import ru.yandex.practicum.main.service.exception.NotFoundException;
import ru.yandex.practicum.main.service.mapper.RequestMapper;
import ru.yandex.practicum.main.service.model.Event;
import ru.yandex.practicum.main.service.model.Request;
import ru.yandex.practicum.main.service.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class RequestService implements RequestServiceInt {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;


    @Override
    public ParticipationRequestDto createRequest(Integer userId, Integer eventId) {
        ParticipationRequestDto requestDto;
        Integer confirmedRequester = 0;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Request already exists");
        }
        if (Objects.equals(userId, event.getInitiator().getId())) {
            throw new ConflictException("Initiator cannot make a request to his own event");
        }
        if (event.getState().equals(String.valueOf(State.PENDING)) || event.getState().equals(String.valueOf(State.CANCELED))) {
            throw new ConflictException("event is not available");
        }
        if (Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests()) && event.getParticipantLimit() != 0) {
            throw new ConflictException("Participation limit is already reached");
        }
        if (event.getParticipantLimit() == 0) {
            requestDto = requestMapper.toDto(userId, eventId, LocalDateTime.now(), Status.CONFIRMED);
            confirmedRequester = 1;
        } else {
            requestDto = requestMapper.toDto(userId, eventId, LocalDateTime.now(), Status.PENDING);
        }
        if (!event.getRequestModeration()) {
            requestDto.setStatus(String.valueOf(Status.CONFIRMED));
            confirmedRequester = 1;
        }

        if (confirmedRequester == 1) {
            event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequester);
            eventRepository.save(event);
        }


        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));

        Request request = requestMapper.toRequest(requestDto, requester, event);
        Request savedRequest = requestRepository.save(request);
        return requestMapper.toDto(savedRequest, Status.valueOf(requestDto.getStatus()));
    }


    @Override
    public ParticipationRequestDto cancelParticipation(Integer userId, Integer requestId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("request not found"));
        request.setStatus(Status.CANCELED);
        requestRepository.save(request);
        return requestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Integer userId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        List<Request> userRequests = requestRepository.getUserRequests(userId);
        return userRequests.stream()
                .map(requestMapper::toDto)
                .toList();
    }

}
