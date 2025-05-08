package ru.yandex.practicum.main.service.dto;

import lombok.Data;

@Data
public class UserWithLikesShortDto {
    private Integer id;
    private String name;
    private Long likes;
}
