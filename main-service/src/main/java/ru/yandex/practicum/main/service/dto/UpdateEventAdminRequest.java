package ru.yandex.practicum.main.service.dto;

import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
public class UpdateEventAdminRequest {
    @Length(max = 2000, min = 20)
    private String annotation;
    private Integer category;
    @Length(max = 7000, min = 20)
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @Positive
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Length(max = 120, min = 3)
    private String title;
}
