package ru.yandex.practicum.main.service.core.repository.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.main.service.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e FROM Event e " +
            "WHERE (?1 IS NULL OR e.id IN ?1)")
    List<Event> findEventsByIds(List<Integer> eventIds);

    @Query("SELECT e FROM Event e WHERE e.category.name = :categoryName")
    List<Event> findByCategoryName(@Param("categoryName") String categoryName);


    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id = ?1 " +
            "ORDER BY e.id ASC")
    List<Event> getUserEvents(Integer userId, Pageable pageable);


    @Query("SELECT e FROM Event e " +
            "WHERE e.initiator.id = ?1 " +
            "AND e.id = ?2")
    Event getUserEvent(Integer userId, Integer eventId);


}
