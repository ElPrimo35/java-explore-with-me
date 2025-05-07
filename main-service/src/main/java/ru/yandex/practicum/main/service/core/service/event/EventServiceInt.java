package ru.yandex.practicum.main.service.core.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.yandex.practicum.main.service.dto.*;

import java.util.List;

public interface EventServiceInt {
    EventFullDto createEvent(EventRequestDto eventRequestDto, Integer userId, HttpServletRequest request);

    List<EventFullDto> getAllEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                    String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto updateEvent(UpdateEventAdminRequest updateEventAdminRequest, Integer eventId);

    List<EventFullDto> getUserEvents(Integer userId, Integer from, Integer size, HttpServletRequest request);


    EventFullDto getUserEvent(Integer userId, Integer eventId);

    List<ParticipationRequestDto> getRequestsUsersEvent(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult requestConfirmation(Integer userId, Integer eventId, EventRequestStatusUpdateRequest request);

    EventFullDto updateUserEvent(Integer userId, Integer eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getEvents(String text,
                                  List<Integer> categories,
                                  Boolean paid, String rangeStart,
                                  String rangeEnd,
                                  Boolean onlyAvailable,
                                  String sort,
                                  Integer from,
                                  Integer size,
                                  HttpServletRequest request);

    EventFullDto getEvent(Integer eventId, HttpServletRequest request);
}
