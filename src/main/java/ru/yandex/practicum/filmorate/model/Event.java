package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.enumeration.EventType;
import ru.yandex.practicum.filmorate.enumeration.Operation;

@Data
public class Event {

    private Integer eventId;
    private Long timestamp;
    private Integer userId;
    private EventType eventType;
    private Operation operation;
    private Integer entityId;
}