package ru.yandex.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.main.service.dto.CompilationDto;
import ru.yandex.practicum.main.service.dto.CompilationRequestDto;
import ru.yandex.practicum.main.service.dto.CompilationShortDto;
import ru.yandex.practicum.main.service.dto.EventShortDto;
import ru.yandex.practicum.main.service.model.Compilation;

import java.util.List;

@Component
public class CompilationMapper {
    public Compilation toEntity(CompilationRequestDto compilationRequestDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationRequestDto.getPinned() != null ? compilationRequestDto.getPinned() : false);
        compilation.setTitle(compilationRequestDto.getTitle());
        return compilation;
    }

    public CompilationDto toCompilationDto(CompilationRequestDto requestDto, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setEvents(events);
        compilationDto.setTitle(requestDto.getTitle());
        compilationDto.setPinned(requestDto.getPinned());
        return compilationDto;
    }

    public CompilationDto toCompilationDto(String title, Boolean pinned, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setEvents(events);
        compilationDto.setTitle(title);
        compilationDto.setPinned(pinned);
        return compilationDto;
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(events);
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        return compilationDto;
    }

    public CompilationShortDto toShortDto(CompilationRequestDto requestDto) {
        CompilationShortDto compilationShortDto = new CompilationShortDto();
        compilationShortDto.setPinned(requestDto.getPinned());
        compilationShortDto.setTitle(requestDto.getTitle());
        return compilationShortDto;
    }
}
