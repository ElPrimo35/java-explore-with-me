package ru.yandex.practicum.main.service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.main.service.dto.LikeId;

@Entity
@Table(name = "likes")
@IdClass(LikeId.class)
@Getter
@Setter
public class Like {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "event_id")
    private Long eventId;

}