package ru.yandex.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.main.service.model.User;

@Data
@AllArgsConstructor
public class UserWithDislikesFullDto {
    private User user;
    private Long dislikes;
}
