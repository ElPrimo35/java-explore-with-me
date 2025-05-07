package ru.yandex.practicum.main.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    @Length(max = 50)
    @NotBlank
    private String name;
}
