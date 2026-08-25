package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.EventDto;
import ru.yandex.practicum.filmorate.enumeration.EventType;
import ru.yandex.practicum.filmorate.enumeration.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.EventMapper;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    public void createEvent(Integer userId, EventType eventType, Operation operation, Integer entityId) {
        Event event = new Event();
        event.setTimestamp(System.currentTimeMillis());
        event.setUserId(userId);
        event.setEventType(eventType);
        event.setOperation(operation);
        event.setEntityId(entityId);
        eventStorage.addEvent(event);
    }

    public List<EventDto> getFeed(Integer userId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + " не найден"));
        return eventStorage.getFeed(userId).stream().map(EventMapper::maptoEventDto).toList();
    }
}
