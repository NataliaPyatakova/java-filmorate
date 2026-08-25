package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

@Component
public class EventDbStorage extends BaseStorage<Event> implements EventStorage {

    private static final String INSERT_QUERY = """
            INSERT INTO events (timestamp, user_id, event_type, operation, entity_id)
            VALUES (?, ?, ?, ?, ?)
            """;
    private static final String FIND_FEEDS_QUERY = """
            SELECT *
            FROM events
            WHERE user_id = ?
            ORDER BY timestamp ASC
            """;

    public EventDbStorage(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addEvent(Event event) {
        int id = insert(INSERT_QUERY,
                event.getTimestamp(),
                event.getUserId(),
                event.getEventType().name(),
                event.getOperation().name(),
                event.getEntityId()
        );

        event.setEventId(id);
    }

    @Override
    public List<Event> getFeed(Integer userId) {
        return findMany(FIND_FEEDS_QUERY, userId);
    }
}
