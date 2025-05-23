package ru.yandex.practicum.main.service.core.service.compilation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.main.service.core.repository.compilation.CompilationRepository;
import ru.yandex.practicum.main.service.core.repository.event.EventRepository;
import ru.yandex.practicum.main.service.dto.CompilationDto;
import ru.yandex.practicum.main.service.dto.CompilationRequestDto;
import ru.yandex.practicum.main.service.dto.CompilationUpdateDto;
import ru.yandex.practicum.main.service.dto.EventShortDto;
import ru.yandex.practicum.main.service.exception.NotFoundException;
import ru.yandex.practicum.main.service.mapper.CategoryMapper;
import ru.yandex.practicum.main.service.mapper.CompilationMapper;
import ru.yandex.practicum.main.service.mapper.EventMapper;
import ru.yandex.practicum.main.service.mapper.UserMapper;
import ru.yandex.practicum.main.service.model.Compilation;
import ru.yandex.practicum.main.service.model.Event;
import ru.yandex.practicum.stats.client.StatsClient;
import ru.yandex.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CompilationService implements CompilationServiceInt {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final StatsClient statsClient;

    @Override
    @Transactional
    public CompilationDto createComp(CompilationRequestDto requestDto, HttpServletRequest request) {
        List<Event> eventList = eventRepository.findEventsByIds(requestDto.getEvents());
        Compilation compilation = compilationMapper.toEntity(requestDto);
        Compilation savedCompilation = compilationRepository.save(compilation);
        eventList.forEach(event -> {
            compilationRepository.insertCompilationEvent(
                    savedCompilation.getId(),
                    event.getId()
            );
        });

        List<EventShortDto> eventShortDtos = eventList.stream()
                .map(event -> eventMapper.toShortDto(event,
                        categoryMapper.toDto(event.getCategory()),
                        userMapper.toShortDto(event.getInitiator()),
                        getViewStats(event, request.getRequestURI())))
                .toList();
        return compilationMapper.toCompilationDto(compilation, eventShortDtos);
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

    @Override
    @Transactional
    public void deleteCompilation(Integer compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        compilationRepository.deleteCompilationEvents(compilation.getId());
        compilationRepository.deleteById(compilation.getId());
    }


    @Override
    @Transactional
    public CompilationDto updateComp(CompilationUpdateDto requestDto, Integer compId, HttpServletRequest request) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        List<Event> events = eventRepository.findEventsByIds(requestDto.getEvents());
        compilationRepository.deleteCompilationEvents(compId);
        events.forEach(event -> {
            compilationRepository.insertCompilationEvent(
                    compId,
                    event.getId()
            );
        });
        List<Event> newEvents = compilationRepository.findEventsByCompId(compId);
        List<EventShortDto> eventShortDtos = newEvents.stream()
                .map(event -> eventMapper.toShortDto(event,
                        categoryMapper.toDto(event.getCategory()),
                        userMapper.toShortDto(event.getInitiator()),
                        getViewStats(event, request.getRequestURI())))
                .toList();
        return compilationMapper.toCompilationDto(compilation, eventShortDtos);
    }


    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size, HttpServletRequest request) {
        int pageNumber = from >= 0 ? from : 0;
        int pageSize = size > 0 ? size : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Compilation> compilations = compilationRepository.findAll(pageable);
        return compilations.stream()
                .map(compilation -> compilationMapper.toCompilationDto(
                        compilation,
                        compilationRepository.findEventsByCompId(compilation.getId()).stream()
                                .map(event -> eventMapper.toShortDto(event,
                                        categoryMapper.toDto(event.getCategory()),
                                        userMapper.toShortDto(event.getInitiator()),
                                        getViewStats(event, request.getRequestURI())))
                                .toList()
                )).toList();
    }


    @Override
    public CompilationDto getCompById(Integer compId, HttpServletRequest request) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found"));
        return compilationMapper.toCompilationDto(
                compilation,
                compilationRepository.findEventsByCompId(compilation.getId()).stream()
                        .map(event -> eventMapper.toShortDto(event,
                                categoryMapper.toDto(event.getCategory()),
                                userMapper.toShortDto(event.getInitiator()),
                                getViewStats(event, request.getRequestURI())))
                        .toList()
        );
    }

}
