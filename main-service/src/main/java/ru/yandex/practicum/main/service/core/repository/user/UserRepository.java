package ru.yandex.practicum.main.service.core.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.main.service.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u " +
            "WHERE (?1 IS NULL OR u.id IN ?1) " +
            "ORDER BY u.id ASC")
    List<User> getUsers(List<Integer> ids, Pageable pageable);

    boolean existsByEmail(String email);

}
