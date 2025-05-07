package ru.yandex.practicum.main.service.core.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.main.service.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    @Query("SELECT r FROM Request r " +
            "WHERE r.event.id = ?2 " +
            "AND r.event.initiator.id = ?1")
    List<Request> getRequestsUsersEvent(Integer userId, Integer eventId);


    List<Request> findByIdIn(List<Integer> requestIds);

    @Query("SELECT r FROM Request r " +
            "WHERE r.requester.id = ?1")
    List<Request> getUserRequests(Integer userId);


    boolean existsByRequesterIdAndEventId(Integer requesterId, Integer eventId);
}
