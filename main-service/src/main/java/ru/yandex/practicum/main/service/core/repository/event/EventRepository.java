package ru.yandex.practicum.main.service.core.repository.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.main.service.dto.EventStatsDto;
import ru.yandex.practicum.main.service.dto.EventWithDislikesFullDto;
import ru.yandex.practicum.main.service.dto.EventWithDislikesShortDto;
import ru.yandex.practicum.main.service.dto.EventWithLikesFullDto;
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

    @Modifying
    @Query(value = "INSERT INTO likes (user_id, event_id) VALUES (?1, ?2)",
            nativeQuery = true)
    void addLike(Integer userid, Integer eventId);

    @Modifying
    @Query(value = "INSERT INTO dislikes (user_id, event_id) VALUES (?1, ?2)",
            nativeQuery = true)
    void addDislike(Integer userid, Integer eventId);


    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.EventWithLikesFullDto(e, COUNT(l.eventId)) " +
            "FROM Event e LEFT JOIN Like l ON e.id = l.eventId " +
            "GROUP BY e " +
            "ORDER BY COUNT(l.eventId) DESC")
    List<EventWithLikesFullDto> getEventsSortedByLikesDesc();

    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.EventWithLikesFullDto(e, COUNT(l.eventId)) " +
            "FROM Event e LEFT JOIN Like l ON e.id = l.eventId " +
            "GROUP BY e " +
            "ORDER BY COUNT(l.eventId) ASC")
    List<EventWithLikesFullDto> getEventsSortedByLikesAsc();


    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.EventWithDislikesFullDto(e, COUNT(l.eventId)) " +
            "FROM Event e LEFT JOIN Dislike l ON e.id = l.eventId " +
            "GROUP BY e " +
            "ORDER BY COUNT(l.eventId) DESC")
    List<EventWithDislikesFullDto> getEventsSortedByDislikesDesc();

    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.EventWithDislikesFullDto(e, COUNT(l.eventId)) " +
            "FROM Event e LEFT JOIN Dislike l ON e.id = l.eventId " +
            "GROUP BY e " +
            "ORDER BY COUNT(l.eventId) Asc")
    List<EventWithDislikesFullDto> getEventsSortedByDislikesAsc();

    @Query("SELECT NEW ru.yandex.practicum.main.service.dto.EventStatsDto(e, " +
            "(SELECT COUNT(l) FROM Like l WHERE l.eventId = e.id), " +
            "(SELECT COUNT(d) FROM Dislike d WHERE d.eventId = e.id)) " +
            "FROM Event e " +
            "WHERE e.id = ?1")
    EventStatsDto getEventWithStats(Integer eventId);

}
