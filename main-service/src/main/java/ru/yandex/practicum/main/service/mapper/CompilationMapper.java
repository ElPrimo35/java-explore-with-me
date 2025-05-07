package ru.yandex.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.main.service.dto.CompilationDto;
import ru.yandex.practicum.main.service.dto.CompilationRequestDto;
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

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        CompilationDto compilationDto = new CompilationDto();
        compilationDto.setId(compilation.getId());
        compilationDto.setEvents(events);
        compilationDto.setTitle(compilation.getTitle());
        compilationDto.setPinned(compilation.getPinned());
        return compilationDto;
    }

}
