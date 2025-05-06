package ru.yandex.practicum.main.service.core.service.compilation;

import ru.yandex.practicum.main.service.dto.CompilationDto;
import ru.yandex.practicum.main.service.dto.CompilationRequestDto;
import ru.yandex.practicum.main.service.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationServiceInt {
    CompilationDto createComp(CompilationRequestDto requestDto);

    void deleteCompilation(Integer compId);

    CompilationDto updateComp(CompilationUpdateDto requestDto, Integer compId);

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompById(Integer compId);
}
