package ru.yandex.practicum.main.service.core.service.event;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.main.service.dto.*;

import java.util.List;

public interface EventServiceInt {
    EventFullDto createEvent(EventRequestDto eventRequestDto, Integer userId, HttpServletRequest request);

    List<EventFullDto> getAllEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                    String rangeStart, String rangeEnd, Integer from, Integer size, HttpServletRequest request);

    EventFullDto updateEvent(UpdateEventAdminRequest updateEventAdminRequest, Integer eventId, HttpServletRequest request);

    List<EventFullDto> getUserEvents(Integer userId, Integer from, Integer size, HttpServletRequest request);


    EventFullDto getUserEvent(Integer userId, Integer eventId, HttpServletRequest request);

    List<ParticipationRequestDto> getRequestsUsersEvent(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult requestConfirmation(Integer userId, Integer eventId, EventRequestStatusUpdateRequest request);

    EventFullDto updateUserEvent(Integer userId, Integer eventId, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request);

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

    EventWithLikesShortDto addLike(Integer userId, Integer eventId, HttpServletRequest request);

    EventWithDislikesShortDto addDislike(Integer userId, Integer eventId, HttpServletRequest request);

    List<EventWithLikesShortDto> getEventRatingByLikes(SortStrategyLikes sortStrategy, HttpServletRequest request);

    List<EventWithDislikesShortDto> getEventRatingByDislikes(SortStrategyDislikes sortStrategy, HttpServletRequest request);

    EventStatsShortDto getEventWithStats(Integer eventId, HttpServletRequest request);
}
