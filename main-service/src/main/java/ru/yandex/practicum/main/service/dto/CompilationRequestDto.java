package ru.yandex.practicum.main.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CompilationRequestDto {
    private List<Integer> events;
    private Boolean pinned;
    @Length(max = 50)
    @NonNull
    @NotBlank
    private String title;
}
