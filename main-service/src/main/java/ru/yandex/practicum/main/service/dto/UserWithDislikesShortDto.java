package ru.yandex.practicum.main.service.dto;

import lombok.Data;

@Data
public class UserWithDislikesShortDto {
    private Integer id;
    private String name;
    private Long dislikes;
}
