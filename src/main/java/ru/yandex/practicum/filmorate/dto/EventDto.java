package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.enumeration.EventType;
import ru.yandex.practicum.filmorate.enumeration.Operation;

@Data
@Accessors(chain = true)
public class EventDto {

    private Integer eventId;
    private Long timestamp;
    private Integer userId;
    private EventType eventType;
    private Operation operation;
    private Integer entityId;
}
