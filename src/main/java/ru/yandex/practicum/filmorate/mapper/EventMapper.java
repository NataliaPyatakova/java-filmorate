package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.model.Event;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static EventDto maptoEventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setEventId(event.getEventId());
        eventDto.setUserId(event.getUserId());
        eventDto.setEventType(event.getEventType());
        eventDto.setOperation(event.getOperation());
        eventDto.setEntityId(event.getEntityId());
        eventDto.setTimestamp(event.getTimestamp());
        return eventDto;
    }
}
