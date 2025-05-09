package ru.yandex.practicum.main.service.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.main.service.dto.UserDto;
import ru.yandex.practicum.main.service.dto.UserShortDto;
import ru.yandex.practicum.main.service.dto.UserWithDislikesShortDto;
import ru.yandex.practicum.main.service.dto.UserWithLikesShortDto;
import ru.yandex.practicum.main.service.model.User;

@Component
public class UserMapper {
    public User toUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public UserShortDto toShortDto(User user) {
        UserShortDto userDto = new UserShortDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }

    public UserWithLikesShortDto toShortDtoWithLikes(User user, Long likes) {
        UserWithLikesShortDto userWithLikesShortDto = new UserWithLikesShortDto();
        userWithLikesShortDto.setId(user.getId());
        userWithLikesShortDto.setName(user.getName());
        userWithLikesShortDto.setLikes(likes);
        return userWithLikesShortDto;
    }


    public UserWithDislikesShortDto toShortDtoWithDislikes(User user, Long dislikes) {
        UserWithDislikesShortDto userWithDislikesShortDto = new UserWithDislikesShortDto();
        userWithDislikesShortDto.setId(user.getId());
        userWithDislikesShortDto.setName(user.getName());
        userWithDislikesShortDto.setDislikes(dislikes);
        return userWithDislikesShortDto;
    }
}
