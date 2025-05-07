package ru.yandex.practicum.main.service.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CompilationUpdateDto {
    private List<Integer> events;
    private Boolean pinned;
    @Length(max = 50)
    private String title;
}
