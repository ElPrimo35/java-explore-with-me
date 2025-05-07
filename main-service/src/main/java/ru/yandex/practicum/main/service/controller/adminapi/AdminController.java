package ru.yandex.practicum.main.service.controller.adminapi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.main.service.core.service.category.CategoryServiceInt;
import ru.yandex.practicum.main.service.core.service.compilation.CompilationServiceInt;
import ru.yandex.practicum.main.service.core.service.event.EventServiceInt;
import ru.yandex.practicum.main.service.core.service.user.UserServiceInt;
import ru.yandex.practicum.main.service.dto.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserServiceInt userService;
    private final CategoryServiceInt categoryService;
    private final CompilationServiceInt compilationService;
    private final EventServiceInt eventService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) List<Integer> ids,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }


    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return categoryService.createCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer catId) {
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@RequestBody @Valid CategoryDto categoryDto, @PathVariable Integer catId) {
        return categoryService.updateCategory(categoryDto, catId);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createComp(@RequestBody @Valid CompilationRequestDto requestDto, HttpServletRequest request) {
        return compilationService.createComp(requestDto, request);
    }

    @GetMapping("/events")
    public List<EventFullDto> getAllEvents(@RequestParam(required = false) List<Integer> users,
                                           @RequestParam(required = false) List<String> states,
                                           @RequestParam(required = false) List<Integer> categories,
                                           @RequestParam(required = false) String rangeStart,
                                           @RequestParam(required = false) String rangeEnd,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size, HttpServletRequest request) {
        return eventService.getAllEvents(users, states, categories, rangeStart, rangeEnd, from, size, request);
    }


    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer compId) {
        compilationService.deleteCompilation(compId);
    }


    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateComp(@RequestBody @Valid CompilationUpdateDto requestDto,
                                     @PathVariable Integer compId, HttpServletRequest request) {
        return compilationService.updateComp(requestDto, compId, request);
    }


    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest, @PathVariable Integer eventId, HttpServletRequest request) {
        return eventService.updateEvent(updateEventAdminRequest, eventId, request);
    }
}
