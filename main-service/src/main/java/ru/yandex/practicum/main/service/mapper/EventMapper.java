package ru.yandex.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.main.service.dto.*;
import ru.yandex.practicum.main.service.model.Category;
import ru.yandex.practicum.main.service.model.Event;
import ru.yandex.practicum.main.service.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto toFullDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto, Location location) {
        EventFullDto eventFullDto = new EventFullDto();
        eventFullDto.setId(event.getId());
        eventFullDto.setAnnotation(event.getAnnotation());
        eventFullDto.setCategory(categoryDto);
        eventFullDto.setConfirmedRequests(event.getConfirmedRequests());
        eventFullDto.setCreatedOn(event.getCreatedOn().format(formatter));
        eventFullDto.setDescription(event.getDescription());
        eventFullDto.setEventDate(event.getEventDate().format(formatter));
        eventFullDto.setInitiator(userShortDto);
        eventFullDto.setLocation(location);
        eventFullDto.setPaid(event.getPaid());
        eventFullDto.setParticipantLimit(event.getParticipantLimit());
        eventFullDto.setPublishedOn(String.valueOf(event.getPublishedOn()));
        eventFullDto.setRequestModeration(event.getRequestModeration());
        eventFullDto.setState(event.getState());
        eventFullDto.setTitle(event.getTitle());
        eventFullDto.setViews(Math.toIntExact(event.getViews()));
        return eventFullDto;
    }

    public Event toEntity(EventRequestDto eventRequestDto, Category category,
                          User user, Long views, Integer confirmedRequest, LocalDateTime createdOn) {
        Event event = new Event();
        event.setAnnotation(eventRequestDto.getAnnotation());
        event.setCategory(category);
        event.setConfirmedRequests(confirmedRequest);
        event.setCreatedOn(createdOn);
        event.setDescription(eventRequestDto.getDescription());
        event.setEventDate(LocalDateTime.parse(eventRequestDto.getEventDate(), formatter));
        event.setInitiator(user);
        event.setLocationLat(eventRequestDto.getLocation().getLat());
        event.setLocationLon(eventRequestDto.getLocation().getLon());
        event.setPaid(eventRequestDto.getPaid());
        event.setParticipantLimit(eventRequestDto.getParticipantLimit());
        event.setPublishedOn(null);
        event.setRequestModeration(eventRequestDto.getRequestModeration());
        event.setState("PENDING");
        event.setTitle(eventRequestDto.getTitle());
        event.setViews(views);
        return event;
    }

    public EventShortDto toShortDto(Event event, CategoryDto category, UserShortDto userShortDto) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(category);
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate().format(formatter));
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(userShortDto);
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(Math.toIntExact(event.getViews()));
        return eventShortDto;
    }
}































