package ru.yandex.practicum.main.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    @Length(max = 50)
    @NonNull
    @NotBlank
    private String name;
}
