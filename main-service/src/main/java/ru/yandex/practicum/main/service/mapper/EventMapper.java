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

    public EventFullDto toFullDto(Event event, CategoryDto categoryDto, UserShortDto userShortDto, Location location, Long views) {
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
        eventFullDto.setViews(Math.toIntExact(views));
        return eventFullDto;
    }

    public Event toEntity(EventRequestDto eventRequestDto, Category category,
                          User user, Integer confirmedRequest, LocalDateTime createdOn) {
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
        return event;
    }

    public EventShortDto toShortDto(Event event, CategoryDto category, UserShortDto userShortDto, Long views) {
        EventShortDto eventShortDto = new EventShortDto();
        eventShortDto.setAnnotation(event.getAnnotation());
        eventShortDto.setCategory(category);
        eventShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventShortDto.setEventDate(event.getEventDate().format(formatter));
        eventShortDto.setId(event.getId());
        eventShortDto.setInitiator(userShortDto);
        eventShortDto.setPaid(event.getPaid());
        eventShortDto.setTitle(event.getTitle());
        eventShortDto.setViews(Math.toIntExact(views));
        return eventShortDto;
    }


    public EventWithLikesShortDto toShortDtoWithLikes(Event event,
                                                      CategoryDto category,
                                                      UserShortDto userShortDto,
                                                      Long views,
                                                      Integer likes) {
        EventWithLikesShortDto eventLikesDto = new EventWithLikesShortDto();
        eventLikesDto.setAnnotation(event.getAnnotation());
        eventLikesDto.setCategory(category);
        eventLikesDto.setConfirmedRequests(event.getConfirmedRequests());
        eventLikesDto.setEventDate(event.getEventDate().format(formatter));
        eventLikesDto.setId(event.getId());
        eventLikesDto.setInitiator(userShortDto);
        eventLikesDto.setPaid(event.getPaid());
        eventLikesDto.setTitle(event.getTitle());
        eventLikesDto.setViews(Math.toIntExact(views));
        eventLikesDto.setLikes(likes);
        return eventLikesDto;
    }

    public EventWithDislikesShortDto toShortDtoWithDislikes(Event event,
                                                      CategoryDto category,
                                                      UserShortDto userShortDto,
                                                      Long views,
                                                      Integer dislikes) {
        EventWithDislikesShortDto eventDislikesDto = new EventWithDislikesShortDto();
        eventDislikesDto.setAnnotation(event.getAnnotation());
        eventDislikesDto.setCategory(category);
        eventDislikesDto.setConfirmedRequests(event.getConfirmedRequests());
        eventDislikesDto.setEventDate(event.getEventDate().format(formatter));
        eventDislikesDto.setId(event.getId());
        eventDislikesDto.setInitiator(userShortDto);
        eventDislikesDto.setPaid(event.getPaid());
        eventDislikesDto.setTitle(event.getTitle());
        eventDislikesDto.setViews(Math.toIntExact(views));
        eventDislikesDto.setDislikes(dislikes);
        return eventDislikesDto;
    }

    public EventStatsShortDto toStatsShortDto(Event event,
                                                CategoryDto category,
                                                UserShortDto userShortDto,
                                                Long views,
                                                Integer dislikes,
                                                Integer likes) {
        EventStatsShortDto eventStatsShortDto = new EventStatsShortDto();
        eventStatsShortDto.setAnnotation(event.getAnnotation());
        eventStatsShortDto.setCategory(category);
        eventStatsShortDto.setConfirmedRequests(event.getConfirmedRequests());
        eventStatsShortDto.setEventDate(event.getEventDate().format(formatter));
        eventStatsShortDto.setId(event.getId());
        eventStatsShortDto.setInitiator(userShortDto);
        eventStatsShortDto.setPaid(event.getPaid());
        eventStatsShortDto.setTitle(event.getTitle());
        eventStatsShortDto.setViews(Math.toIntExact(views));
        eventStatsShortDto.setDislikes(dislikes);
        eventStatsShortDto.setLikes(likes);
        return eventStatsShortDto;
    }
}