package ru.yandex.practicum.main.service.core.repository.compilation;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.main.service.model.Compilation;
import ru.yandex.practicum.main.service.model.Event;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    @Query(value = "SELECT e.* FROM events e " +
            "JOIN compilation_events ce ON e.id = ce.event_id " +
            "WHERE ce.compilation_id = ?1",
            nativeQuery = true)
    List<Event> findEventsByCompId(Integer compId);


    @Modifying
    @Query(value = "INSERT INTO compilation_events (compilation_id, event_id) VALUES (?1, ?2)",
            nativeQuery = true)
    void insertCompilationEvent(Integer compilationId, Integer eventId);

    @Modifying
    @Transactional
    @Query(value = "SET CONSTRAINTS ALL DEFERRED; " +
            "DELETE FROM compilation_events WHERE compilation_id = ?1;",
            nativeQuery = true)
    void deleteCompilationEvents(Integer compId);
}
