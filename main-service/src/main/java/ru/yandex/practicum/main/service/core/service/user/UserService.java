package ru.yandex.practicum.main.service.core.service.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.main.service.core.repository.user.UserRepository;
import ru.yandex.practicum.main.service.dto.*;
import ru.yandex.practicum.main.service.exception.ConflictException;
import ru.yandex.practicum.main.service.mapper.UserMapper;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService implements UserServiceInt {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("This email is already in use");
        }
        return userMapper.toDto(userRepository.save(userMapper.toUser(userDto)));
    }

    @Override
    public List<UserDto> getUsers(List<Integer> ids, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        return userRepository.getUsers(ids, pageable).stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserWithLikesShortDto> getInitiatorsRatingByLikes(SortStrategyLikes sortStrategy) {
        List<UserWithLikesFullDto> userWithLikesFullDtoList;
        if (sortStrategy.equals(SortStrategyLikes.SORT_BY_LIKES_DESC)) {
            userWithLikesFullDtoList = userRepository.getUsersSortedByLikesDesc();
        } else {
            userWithLikesFullDtoList = userRepository.getUsersSortedByLikesAsc();
        }

        List<UserWithLikesShortDto> userWithLikesShortDtoList = userWithLikesFullDtoList.stream()
                .map(userWithLikesFullDto -> userMapper.toShortDtoWithLikes(
                        userWithLikesFullDto.getUser(),
                        userWithLikesFullDto.getLikes()
                )).toList();
        return userWithLikesShortDtoList;
    }

    @Override
    public List<UserWithDislikesShortDto> getInitiatorsRatingByDislikes(SortStrategyDislikes sortStrategy) {
        List<UserWithDislikesFullDto> userWithDislikesFullDtoList;
        if (sortStrategy.equals(SortStrategyDislikes.SORT_BY_DISLIKES_DESC)) {
            userWithDislikesFullDtoList = userRepository.getUsersSortedByDislikesDesc();
        } else {
            userWithDislikesFullDtoList = userRepository.getUsersSortedByDislikesAsc();
        }
        List<UserWithDislikesShortDto> userWithDislikesShortDtoList = userWithDislikesFullDtoList.stream()
                .map(userWithDislikesFullDto -> userMapper.toShortDtoWithDislikes(
                        userWithDislikesFullDto.getUser(),
                        userWithDislikesFullDto.getDislikes()
                )).toList();
        return userWithDislikesShortDtoList;
    }
}
