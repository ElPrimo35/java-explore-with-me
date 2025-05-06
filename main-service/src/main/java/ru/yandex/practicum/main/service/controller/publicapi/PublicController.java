package ru.yandex.practicum.main.service.controller.publicapi;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.main.service.core.service.category.CategoryServiceInt;
import ru.yandex.practicum.main.service.core.service.compilation.CompilationServiceInt;
import ru.yandex.practicum.main.service.core.service.event.EventServiceInt;
import ru.yandex.practicum.main.service.dto.CategoryDto;
import ru.yandex.practicum.main.service.dto.CompilationDto;
import ru.yandex.practicum.main.service.dto.EventFullDto;
import ru.yandex.practicum.main.service.dto.EventShortDto;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {
    private final CategoryServiceInt categoryService;
    private final CompilationServiceInt compilationService;
    private final EventServiceInt eventService;


    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Integer catId) {
        return categoryService.getCategoryById(catId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        return compilationService.getAllCompilations(pinned, from, size);
    }


    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompById(@PathVariable Integer compId) {
        return compilationService.getCompById(compId);
    }


    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable Integer id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }
}





