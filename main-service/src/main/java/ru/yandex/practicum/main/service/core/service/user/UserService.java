package ru.yandex.practicum.main.service.core.service.user;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.main.service.core.repository.user.UserRepository;
import ru.yandex.practicum.main.service.dto.UserDto;
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
}
