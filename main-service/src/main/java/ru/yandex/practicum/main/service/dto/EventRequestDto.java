package ru.yandex.practicum.main.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
public class EventRequestDto {
    @NotBlank
    @Size(max = 2000, min = 20)
    private String annotation;
    @NonNull
    private Integer category;
    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid = false;
    @PositiveOrZero
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @Length(max = 120, min = 3)
    private String title;
}
