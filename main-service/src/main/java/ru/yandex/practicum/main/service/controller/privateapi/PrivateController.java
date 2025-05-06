package ru.yandex.practicum.main.service.controller.privateapi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.main.service.core.service.event.EventServiceInt;
import ru.yandex.practicum.main.service.core.service.request.RequestServiceInt;
import ru.yandex.practicum.main.service.dto.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class PrivateController {
    private final EventServiceInt eventService;
    private final RequestServiceInt requestService;

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@RequestBody @Valid EventRequestDto eventRequestDto, @PathVariable Integer userId, HttpServletRequest request) {
        return eventService.createEvent(eventRequestDto, userId, request);
    }

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Integer userId,
                                                 @RequestParam Integer eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/events")
    public List<EventFullDto> getUserEvents(@PathVariable Integer userId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        return eventService.getUserEvents(userId, from, size, request);
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable Integer userId,
                                     @PathVariable Integer eventId) {
        return eventService.getUserEvent(userId, eventId);
    }


    @PatchMapping("/events/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable Integer userId,
                                        @PathVariable Integer eventId,
                                        @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateUserEvent(userId, eventId, updateEventAdminRequest);
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsUsersEvent(@PathVariable Integer userId,
                                                               @PathVariable Integer eventId) {
        return eventService.getRequestsUsersEvent(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult requestConfirmation(@PathVariable Integer userId,
                                                              @PathVariable Integer eventId,
                                                              @RequestBody(required = false) EventRequestStatusUpdateRequest request) {
        return eventService.requestConfirmation(userId, eventId, request);
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipation(@PathVariable Integer userId,
                                                       @PathVariable Integer requestId) {
        return requestService.cancelParticipation(userId, requestId);
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable Integer userId) {
        return requestService.getUserRequests(userId);
    }


}
