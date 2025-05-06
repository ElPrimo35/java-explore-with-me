package ru.yandex.practicum.main.service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor(force = true)
public class UserDto {
    private Integer id;
    @NonNull
    @NotBlank
    @Length(max = 250, min = 2)
    private String name;
    @NonNull
    @NotBlank
    @Length(max = 254, min = 6)
    @Email
    private String email;
}
