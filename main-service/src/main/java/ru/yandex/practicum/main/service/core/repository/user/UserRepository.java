package ru.yandex.practicum.main.service.core.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.main.service.dto.UserWithDislikesFullDto;
import ru.yandex.practicum.main.service.dto.UserWithLikesFullDto;
import ru.yandex.practicum.main.service.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u " +
            "WHERE (?1 IS NULL OR u.id IN ?1) " +
            "ORDER BY u.id ASC")
    List<User> getUsers(List<Integer> ids, Pageable pageable);

    boolean existsByEmail(String email);

    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.UserWithLikesFullDto(u, COUNT(l.id.eventId)) " +
            "FROM User u LEFT JOIN Event e ON u.id = e.initiator.id " +
            "LEFT JOIN Like l ON e.id = l.id.eventId " +
            "GROUP BY u " +
            "ORDER BY COUNT(l.id.eventId) DESC")
    List<UserWithLikesFullDto> getUsersSortedByLikesDesc();

    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.UserWithLikesFullDto(u, COUNT(l.id.eventId)) " +
            "FROM User u LEFT JOIN Event e ON u.id = e.initiator.id " +
            "LEFT JOIN Like l ON e.id = l.id.eventId " +
            "GROUP BY u " +
            "ORDER BY COUNT(l.id.eventId) ASC")
    List<UserWithLikesFullDto> getUsersSortedByLikesAsc();

    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.UserWithDislikesFullDto(u, COUNT(l.id.eventId)) " +
            "FROM User u LEFT JOIN Event e ON u.id = e.initiator.id " +
            "LEFT JOIN Dislike l ON e.id = l.id.eventId " +
            "GROUP BY u " +
            "ORDER BY COUNT(l.id.eventId) DESC")
    List<UserWithDislikesFullDto> getUsersSortedByDislikesDesc();

    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.UserWithDislikesFullDto(u, COUNT(l.id.eventId)) " +
            "FROM User u LEFT JOIN Event e ON u.id = e.initiator.id " +
            "LEFT JOIN Dislike l ON e.id = l.id.eventId " +
            "GROUP BY u " +
            "ORDER BY COUNT(l.id.eventId) ASC")
    List<UserWithDislikesFullDto> getUsersSortedByDislikesAsc();

}
