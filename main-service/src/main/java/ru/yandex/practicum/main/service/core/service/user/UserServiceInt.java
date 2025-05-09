package ru.yandex.practicum.main.service.core.service.user;

import ru.yandex.practicum.main.service.dto.*;

import java.util.List;

public interface UserServiceInt {
    UserDto createUser(UserDto userDto);

    List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size);

    void deleteUser(Integer userId);

    List<UserWithLikesShortDto> getInitiatorsRatingByLikes(SortStrategyLikes sortStrategy);

    List<UserWithDislikesShortDto> getInitiatorsRatingByDislikes(SortStrategyDislikes sortStrategy);
}
