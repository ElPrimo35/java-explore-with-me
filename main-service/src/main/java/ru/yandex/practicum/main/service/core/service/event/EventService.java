package ru.yandex.practicum.main.service.core.service.event;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.main.service.core.repository.category.CategoryRepository;
import ru.yandex.practicum.main.service.core.repository.event.EventRepository;
import ru.yandex.practicum.main.service.core.repository.request.RequestRepository;
import ru.yandex.practicum.main.service.core.repository.user.UserRepository;
import ru.yandex.practicum.main.service.dto.*;
import ru.yandex.practicum.main.service.exception.BadRequestException;
import ru.yandex.practicum.main.service.exception.ConflictException;
import ru.yandex.practicum.main.service.exception.NotFoundException;
import ru.yandex.practicum.main.service.mapper.CategoryMapper;
import ru.yandex.practicum.main.service.mapper.EventMapper;
import ru.yandex.practicum.main.service.mapper.RequestMapper;
import ru.yandex.practicum.main.service.mapper.UserMapper;
import ru.yandex.practicum.main.service.model.Category;
import ru.yandex.practicum.main.service.model.Event;
import ru.yandex.practicum.main.service.model.Request;
import ru.yandex.practicum.main.service.model.User;
import ru.yandex.practicum.stats.client.StatsClient;
import ru.yandex.practicum.stats.dto.EndpointHit;
import ru.yandex.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class EventService implements EventServiceInt {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final StatsClient statsClient;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;
    private final CategoryMapper categoryMapper;
    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    @Override
    public EventFullDto createEvent(EventRequestDto eventRequestDto, Integer userId, HttpServletRequest request) {
        Category category;
        LocalDateTime createdOn = LocalDateTime.now();
        LocalDateTime eventDade = LocalDateTime.parse(eventRequestDto.getEventDate(), formatter);
        if (eventDade.isBefore(createdOn.plusHours(2))) {
            throw new BadRequestException("Field: eventDate. Error: должно содержать дату, которая еще не наступила.");
        }
        category = categoryRepository.findById(eventRequestDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found"));


        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventMapper.toEntity(eventRequestDto, category, user, 0, createdOn);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.toFullDto(savedEvent, categoryMapper.toDto(savedEvent.getCategory()),
                userMapper.toShortDto(savedEvent.getInitiator()),
                new Location(savedEvent.getLocationLat(), savedEvent.getLocationLon()),
                0L);
    }

    @Override
    public List<EventFullDto> getAllEvents(List<Integer> users, List<String> states, List<Integer> categories,
                                           String rangeStart, String rangeEnd, Integer from, Integer size, HttpServletRequest request) {
        LocalDateTime start = rangeStart != null ? LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime end = rangeEnd != null ? LocalDateTime.parse(rangeEnd, formatter) : null;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (users != null && !users.isEmpty()) {
            predicates.add(event.get("initiator").get("id").in(users));
        }
        if (states != null && !states.isEmpty()) {
            predicates.add(event.get("state").as(String.class).in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            predicates.add(event.get("category").get("id").in(categories));
        }
        if (start != null && end != null) {
            predicates.add(cb.between(event.get("eventDate"), start, end));
        } else if (start != null) {
            predicates.add(cb.greaterThanOrEqualTo(event.get("eventDate"), start));
        } else if (end != null) {
            predicates.add(cb.lessThanOrEqualTo(event.get("eventDate"), end));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(event.get("id")));
        TypedQuery<Event> query = entityManager.createQuery(cq);
        if (from != null && size != null) {
            query.setFirstResult(from);
            query.setMaxResults(size);
        }
        return query.getResultList().stream()
                .map(event1 -> eventMapper.toFullDto(
                        event1,
                        categoryMapper.toDto(event1.getCategory()),
                        userMapper.toShortDto(event1.getInitiator()),
                        new Location(event1.getLocationLat(), event1.getLocationLon()),
                        getViewStats(event1, request.getRequestURI())
                ))
                .toList();
    }

    @Override
    public EventFullDto updateEvent(UpdateEventAdminRequest updateEventAdminRequest, Integer eventId, HttpServletRequest request) {
        Integer categoryId = updateEventAdminRequest.getCategory();
        Category category;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("CategoryNotFound"));
        } else {
            category = null;
        }
        String eventDate = updateEventAdminRequest.getEventDate();
        if (eventDate != null && LocalDateTime.parse(eventDate, formatter).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("event date must be in the future");
        }
        if (event.getState().equals(String.valueOf(State.PUBLISHED)) || event.getState().equals(String.valueOf(State.CANCELED))) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        Event updatedEvent = updateEventFields(updateEventAdminRequest, event, category);
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                updatedEvent.setState(String.valueOf(State.PUBLISHED));
            } else if (updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT)) {
                updatedEvent.setState(String.valueOf(State.CANCELED));
            }
        }
        Event savedEvent = eventRepository.save(updatedEvent);

        return eventMapper.toFullDto(savedEvent,
                categoryMapper.toDto(savedEvent.getCategory()),
                userMapper.toShortDto(savedEvent.getInitiator()),
                new Location(savedEvent.getLocationLat(), savedEvent.getLocationLon()),
                getViewStats(savedEvent, request.getRequestURI()));
    }

    @Override
    public List<EventFullDto> getUserEvents(Integer userId, Integer from, Integer size, HttpServletRequest request) {
        sendHitStats(request);
        Pageable pageable = PageRequest.of(from, size);
        return eventRepository.getUserEvents(userId, pageable).stream()
                .map(event -> eventMapper.toFullDto(
                        event,
                        categoryMapper.toDto(event.getCategory()),
                        userMapper.toShortDto(event.getInitiator()),
                        new Location(event.getLocationLat(), event.getLocationLon()),
                        getViewStats(event, request.getRequestURI())))
                .toList();

    }


    @Override
    public EventFullDto getUserEvent(Integer userId, Integer eventId, HttpServletRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        Event userEvent = eventRepository.getUserEvent(userId, eventId);
        if (userEvent == null) {
            throw new NotFoundException("Event not found");
        }
        return eventMapper.toFullDto(
                userEvent,
                categoryMapper.toDto(userEvent.getCategory()),
                userMapper.toShortDto(userEvent.getInitiator()),
                new Location(event.getLocationLat(), event.getLocationLon()),
                getViewStats(userEvent, request.getRequestURI()));
    }


    @Override
    public List<ParticipationRequestDto> getRequestsUsersEvent(Integer userId, Integer eventId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        List<Request> requests = requestRepository.getRequestsUsersEvent(userId, eventId);
        return requests.stream()
                .map(requestMapper::toDto)
                .toList();
    }


    @Override
    public EventRequestStatusUpdateResult requestConfirmation(Integer userId, Integer eventId, EventRequestStatusUpdateRequest request) {
        Integer confirmedRequestsCount = 0;
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        Event userEvent = eventRepository.getUserEvent(userId, eventId);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        if (userEvent.getParticipantLimit() == 0 || !userEvent.getRequestModeration()) {
            return result;
        }
        List<Request> requests = requestRepository.findByIdIn(request.getRequestIds());
        List<Request> shouldBeEmpty = requests.stream()
                .filter(request1 -> request1.getStatus() != Status.PENDING)
                .toList();
        if (!shouldBeEmpty.isEmpty()) {
            throw new ConflictException("Request must have status PENDING");
        }
        confirmedRequestsCount = requests.size();
        if (userEvent.getParticipantLimit() < userEvent.getConfirmedRequests() + confirmedRequestsCount) {
            throw new ConflictException("The participant limit has been reached");
        }
        userEvent.setConfirmedRequests(userEvent.getConfirmedRequests() + confirmedRequestsCount);
        eventRepository.save(userEvent);
        List<Request> changedStatusRequests = requests.stream()
                .peek(request1 -> request1.setStatus(request.getStatus()))
                .toList();
        List<Request> changedRequests = requestRepository.saveAll(changedStatusRequests);
        List<ParticipationRequestDto> confirmedRequests = changedRequests.stream()
                .filter(request1 -> request1.getStatus() == Status.CONFIRMED)
                .map(requestMapper::toDto)
                .toList();

        List<ParticipationRequestDto> rejectedRequests = changedRequests.stream()
                .filter(request1 -> request1.getStatus() == Status.REJECTED)
                .map(requestMapper::toDto)
                .toList();

        result.setConfirmedRequests(confirmedRequests);
        result.setRejectedRequests(rejectedRequests);
        return result;
    }

    @Override
    public EventFullDto updateUserEvent(Integer userId, Integer eventId, UpdateEventAdminRequest updateEventAdminRequest, HttpServletRequest request) {
        Integer categoryId = updateEventAdminRequest.getCategory();
        Category category;
        Event event = eventRepository.getUserEvent(userId, eventId);
        String eventDate = updateEventAdminRequest.getEventDate();
        if (eventDate != null && LocalDateTime.parse(eventDate, formatter).isBefore(LocalDateTime.now())) {
            throw new BadRequestException("event date must be in the future");
        }
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category Not Found"));
        } else {
            category = null;
        }
        if (event.getState().equals(String.valueOf(State.PUBLISHED))) {
            throw new ConflictException("Event must not be published");
        }
        Event updatedEvent = eventRepository.save(updateEventFields(updateEventAdminRequest, event, category));
        return eventMapper.toFullDto(updatedEvent,
                categoryMapper.toDto(updatedEvent.getCategory()),
                userMapper.toShortDto(updatedEvent.getInitiator()),
                new Location(updatedEvent.getLocationLat(), updatedEvent.getLocationLon()),
                getViewStats(updatedEvent, request.getRequestURI()));
    }


    @Override
    public List<EventShortDto> getEvents(String text,
                                         List<Integer> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Integer from,
                                         Integer size,
                                         HttpServletRequest request) {
        sendHitStats(request);
        LocalDateTime parsedStart = rangeStart != null ?
                LocalDateTime.parse(rangeStart, formatter) : null;
        LocalDateTime parsedEnd = rangeEnd != null ?
                LocalDateTime.parse(rangeEnd, formatter) : null;

        if (parsedStart != null && parsedEnd != null) {
            if (parsedStart.isAfter(parsedEnd)) throw new BadRequestException("Start must be after end");
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> cq = cb.createQuery(Event.class);
        Root<Event> event = cq.from(Event.class);
        List<Predicate> predicates = new ArrayList<>();
        if (text != null && !text.isBlank()) {
            predicates.add(cb.or(
                    cb.like(cb.lower(event.get("annotation")), "%" + text.toLowerCase() + "%"),
                    cb.like(cb.lower(event.get("description")), "%" + text.toLowerCase() + "%")
            ));
        }
        if (categories != null && !categories.isEmpty()) {
            predicates.add(event.get("category").get("id").in(categories));
        }
        if (paid != null) {
            predicates.add(cb.equal(event.get("paid"), paid));
        }
        if (parsedStart != null) {
            predicates.add(cb.greaterThanOrEqualTo(event.get("eventDate"), parsedStart));
        }
        if (parsedEnd != null) {
            predicates.add(cb.lessThanOrEqualTo(event.get("eventDate"), parsedEnd));
        }
        if (onlyAvailable != null && onlyAvailable) {
            predicates.add(cb.or(
                    cb.equal(event.get("participantLimit"), 0),
                    cb.gt(event.get("participantLimit"), event.get("confirmedRequests"))
            ));
        }
        cq.where(predicates.toArray(new Predicate[0]));
        if ("VIEWS".equalsIgnoreCase(sort)) {
            cq.orderBy(cb.desc(event.get("views")));
        } else if ("EVENT_DATE".equalsIgnoreCase(sort)) {
            cq.orderBy(cb.asc(event.get("eventDate")));
        } else {
            cq.orderBy(cb.desc(event.get("id")));
        }
        TypedQuery<Event> query = entityManager.createQuery(cq);
        query.setFirstResult(from);
        query.setMaxResults(size);
        List<Event> events = query.getResultList().stream()
                .filter(event1 -> event1.getState().equals(String.valueOf(State.PUBLISHED)))
                .toList();
        return events.stream()
                .map(event1 -> eventMapper.toShortDto(
                        event1,
                        categoryMapper.toDto(event1.getCategory()),
                        userMapper.toShortDto(event1.getInitiator()),
                        getViewStats(event1, request.getRequestURI())
                ))
                .toList();
    }

    @Override
    public EventFullDto getEvent(Integer eventId, HttpServletRequest request) {
        sendHitStats(request);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        if (!event.getState().equals(String.valueOf(State.PUBLISHED))) {
            throw new NotFoundException("There is no published events");
        }

        Long views = getViewStats(event, request.getRequestURI());

        Integer categoryId = event.getCategory().getId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category not found"));
        CategoryDto categoryDto = categoryMapper.toDto(category);
        UserShortDto userShortDto = userMapper.toShortDto(event.getInitiator());
        Location location = new Location(event.getLocationLat(), event.getLocationLon());
        return eventMapper.toFullDto(event, categoryDto, userShortDto, location, views);
    }

    @Override
    @Transactional
    public EventWithLikesShortDto addLike(Integer userId, Integer eventId, HttpServletRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        try {
            eventRepository.addLike(userId, eventId);
        } catch (RuntimeException e) {
            throw new BadRequestException("User with id " + userId + " already liked event with id " + eventId);
        }
        EventStatsDto eventStatsDto = eventRepository.getEventWithStats(eventId);
        return eventMapper.toShortDtoWithLikes(
                event,
                categoryMapper.toDto(event.getCategory()),
                userMapper.toShortDto(event.getInitiator()),
                getViewStats(event, request.getRequestURI()),
                Math.toIntExact(eventStatsDto.getLikes())
        );
    }

    @Override
    @Transactional
    public EventWithDislikesShortDto addDislike(Integer userId, Integer eventId, HttpServletRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        try {
            eventRepository.addDislike(userId, eventId);
        } catch (RuntimeException e) {
            throw new BadRequestException("User with id " + userId + " already disliked event with id " + eventId);
        }
        EventStatsDto eventStatsDto = eventRepository.getEventWithStats(eventId);
        return eventMapper.toShortDtoWithDislikes(
                event,
                categoryMapper.toDto(event.getCategory()),
                userMapper.toShortDto(event.getInitiator()),
                getViewStats(event, request.getRequestURI()),
                Math.toIntExact(eventStatsDto.getDislikes())
        );
    }

    @Override
    public List<EventWithLikesShortDto> getEventRatingByLikes(SortStrategyLikes sortStrategy, HttpServletRequest request) {
        List<EventWithLikesFullDto> eventWithLikesFullDtos;
        if (sortStrategy.equals(SortStrategyLikes.SORT_BY_LIKES_DESC)) {
            eventWithLikesFullDtos = eventRepository.getEventsSortedByLikesDesc();
        } else {
            eventWithLikesFullDtos = eventRepository.getEventsSortedByLikesAsc();
        }
        List<EventWithLikesShortDto> eventWithLikesShortDtos = eventWithLikesFullDtos.stream()
                .map(event -> eventMapper.toShortDtoWithLikes(
                        event.getEvent(),
                        categoryMapper.toDto(event.getEvent().getCategory()),
                        userMapper.toShortDto(event.getEvent().getInitiator()),
                        getViewStats(event.getEvent(), request.getRequestURI()),
                        Math.toIntExact(event.getLikes())
                )).toList();
        return eventWithLikesShortDtos;
    }

    @Override
    public List<EventWithDislikesShortDto> getEventRatingByDislikes(SortStrategyDislikes sortStrategy, HttpServletRequest request) {
        List<EventWithDislikesFullDto> eventWithDislikesFullDtos;
        if (sortStrategy.equals(SortStrategyDislikes.SORT_BY_DISLIKES_DESC)) {
            eventWithDislikesFullDtos = eventRepository.getEventsSortedByDislikesDesc();
        } else {
            eventWithDislikesFullDtos = eventRepository.getEventsSortedByDislikesAsc();
        }
        List<EventWithDislikesShortDto> eventWithDislikesShortDtos = eventWithDislikesFullDtos.stream()
                .map(event -> eventMapper.toShortDtoWithDislikes(
                        event.getEvent(),
                        categoryMapper.toDto(event.getEvent().getCategory()),
                        userMapper.toShortDto(event.getEvent().getInitiator()),
                        getViewStats(event.getEvent(), request.getRequestURI()),
                        Math.toIntExact(event.getDislikes())
                )).toList();
        return eventWithDislikesShortDtos;
    }

    @Override
    public EventStatsShortDto getEventWithStats(Integer eventId,HttpServletRequest request) {
        eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Event not found"));
        EventStatsDto eventStatsDto = eventRepository.getEventWithStats(eventId);
        EventStatsShortDto eventStatsShortDto = eventMapper.toStatsShortDto(
                eventStatsDto.getEvent(),
                categoryMapper.toDto(eventStatsDto.getEvent().getCategory()),
                userMapper.toShortDto(eventStatsDto.getEvent().getInitiator()),
                getViewStats(eventStatsDto.getEvent(), request.getRequestURI()),
                Math.toIntExact(eventStatsDto.getDislikes()),
                Math.toIntExact(eventStatsDto.getLikes())
        );
        return eventStatsShortDto;
    }


    private Long getViewStats(Event event, String uri) {
        Long views = 0L;

        List<ViewStats> viewStats = statsClient.getStats(event.getCreatedOn(),
                LocalDateTime.now(),
                List.of(uri), true);
        if (!viewStats.isEmpty()) {
            views = viewStats.getFirst().getHits();
        }
        return views;
    }

    private void sendHitStats(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setId(null);
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setApp("main-service");
        endpointHit.setTimestamp(LocalDateTime.now().format(formatter));
        statsClient.saveHit(endpointHit);
    }

    private Event updateEventFields(UpdateEventAdminRequest updateEventAdminRequest, Event event, Category category) {
        LocalDateTime eventDate = updateEventAdminRequest.getEventDate() != null ?
                LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter) : null;
        if (updateEventAdminRequest.getAnnotation() != null)
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        if (updateEventAdminRequest.getCategory() != null) event.setCategory(category);
        if (updateEventAdminRequest.getDescription() != null)
            event.setDescription(updateEventAdminRequest.getDescription());
        if (updateEventAdminRequest.getEventDate() != null) event.setEventDate(eventDate);
        if (updateEventAdminRequest.getLocation() != null) {
            event.setLocationLat(updateEventAdminRequest.getLocation().getLat());
            event.setLocationLon(updateEventAdminRequest.getLocation().getLon());
        }
        if (updateEventAdminRequest.getPaid() != null) event.setPaid(updateEventAdminRequest.getPaid());
        if (updateEventAdminRequest.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        if (updateEventAdminRequest.getRequestModeration() != null)
            event.setRequestModeration(updateEventAdminRequest.getRequestModeration());
        if (updateEventAdminRequest.getStateAction() != null) {
            if (updateEventAdminRequest.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(String.valueOf(State.PENDING));
            } else if (updateEventAdminRequest.getStateAction().equals(StateAction.REJECT_EVENT) ||
                    updateEventAdminRequest.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(String.valueOf(State.CANCELED));
            }
        }
        if (updateEventAdminRequest.getTitle() != null) event.setTitle(updateEventAdminRequest.getTitle());
        return event;
    }

}
