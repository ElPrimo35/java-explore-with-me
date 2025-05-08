package ru.yandex.practicum.main.service.controller.publicapi;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.main.service.core.service.category.CategoryServiceInt;
import ru.yandex.practicum.main.service.core.service.compilation.CompilationServiceInt;
import ru.yandex.practicum.main.service.core.service.event.EventServiceInt;
import ru.yandex.practicum.main.service.core.service.user.UserServiceInt;
import ru.yandex.practicum.main.service.dto.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PublicController {
    private final CategoryServiceInt categoryService;
    private final CompilationServiceInt compilationService;
    private final EventServiceInt eventService;
    private final UserServiceInt userService;


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
                                                   @RequestParam(defaultValue = "10") Integer size, HttpServletRequest request) {
        return compilationService.getAllCompilations(pinned, from, size, request);
    }


    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompById(@PathVariable Integer compId, HttpServletRequest request) {
        return compilationService.getCompById(compId, request);
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
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable Integer id, HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }

    @GetMapping("/events/rating/likes")
    public List<EventWithLikesShortDto> getEventRatingByLikes(@RequestParam(defaultValue = "SORT_BY_LIKES_DESC") SortStrategyLikes sortStrategy,
                                                              HttpServletRequest request) {
        return eventService.getEventRatingByLikes(sortStrategy, request);
    }

    @GetMapping("/events/rating/dislikes")
    public List<EventWithDislikesShortDto> getEventRatingByDislikes(@RequestParam(defaultValue = "SORT_BY_DISLIKES_DESC") SortStrategyDislikes sortStrategy,
                                                                    HttpServletRequest request) {
        return eventService.getEventRatingByDislikes(sortStrategy, request);
    }

    @GetMapping("/users/rating/likes")
    public List<UserWithLikesShortDto> getInitiatorsRatingByLikes(@RequestParam(defaultValue = "SORT_BY_LIKES_DESC") SortStrategyLikes sortStrategy) {
        return userService.getInitiatorsRatingByLikes(sortStrategy);
    }


    @GetMapping("/users/rating/dislikes")
    public List<UserWithDislikesShortDto> getInitiatorsRatingByDislikes(@RequestParam(defaultValue = "SORT_BY_DISLIKES_DESC") SortStrategyDislikes sortStrategy) {
        return userService.getInitiatorsRatingByDislikes(sortStrategy);
    }

    @GetMapping("/events/{eventId}/stats")
    public EventStatsShortDto getEventWithStats(@PathVariable Integer eventId, HttpServletRequest request) {
        return eventService.getEventWithStats(eventId, request);
    }


}





