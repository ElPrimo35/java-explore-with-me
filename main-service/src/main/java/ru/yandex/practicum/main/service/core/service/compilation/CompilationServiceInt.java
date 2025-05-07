package ru.yandex.practicum.main.service.core.service.compilation;

import jakarta.servlet.http.HttpServletRequest;
import ru.yandex.practicum.main.service.dto.CompilationDto;
import ru.yandex.practicum.main.service.dto.CompilationRequestDto;
import ru.yandex.practicum.main.service.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationServiceInt {
    CompilationDto createComp(CompilationRequestDto requestDto, HttpServletRequest request);

    void deleteCompilation(Integer compId);

    CompilationDto updateComp(CompilationUpdateDto requestDto, Integer compId, HttpServletRequest request);

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size, HttpServletRequest request);

    CompilationDto getCompById(Integer compId, HttpServletRequest request);
}
