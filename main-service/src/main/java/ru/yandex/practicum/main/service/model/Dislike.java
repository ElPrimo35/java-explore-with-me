package ru.yandex.practicum.main.service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.main.service.dto.DislikeId;

@Entity
@Table(name = "dislikes")
@IdClass(DislikeId.class)
@Getter
@Setter
public class Dislike {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

}