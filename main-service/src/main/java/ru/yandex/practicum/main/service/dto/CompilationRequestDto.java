package ru.yandex.practicum.main.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CompilationRequestDto {
    private List<Integer> events;
    private Boolean pinned;
    @Length(max = 50)
    @NotBlank
    private String title;
}
