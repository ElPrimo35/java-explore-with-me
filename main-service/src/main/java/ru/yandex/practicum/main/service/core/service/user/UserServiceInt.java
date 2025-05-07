package ru.yandex.practicum.main.service.core.service.user;

import ru.yandex.practicum.main.service.dto.UserDto;

import java.util.List;

public interface UserServiceInt {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size);

    void deleteUser(Integer userId);
}
