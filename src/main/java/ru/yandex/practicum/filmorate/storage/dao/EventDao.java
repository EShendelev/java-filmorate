package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.storage.interfaces.EventStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class EventDao implements EventStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Event add(long userId, long entityId, EventTypes event, EventOperations operation) {
        Event feed = Event.builder()
                .timestamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .userId(userId)
                .entityId(entityId)
                .eventType(event)
                .operation(operation)
                .build();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("event_id");
        long feedId = simpleJdbcInsert.executeAndReturnKey(feedToMap(feed)).longValue();
        return getFeedById(feedId);
    }

    @Override
    public List<Event> getUserEvents(long userId) {
        String sqlQuery = "SELECT * FROM events " +
                "WHERE user_id = ? " +
                "ORDER BY EVENT_TIME";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFeed, userId);
    }

    @Override
    public Event getFeedById(long eventId) {
        String sqlQuery = "SELECT * FROM events WHERE event_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFeed, eventId);
    }

    private Event mapRowToFeed(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(resultSet.getLong("event_id"))
                .timestamp(resultSet.getLong("event_time"))
                .userId(resultSet.getLong("user_id"))
                .eventType(EventTypes.valueOf(resultSet.getString("event_type")))
                .operation(EventOperations.valueOf(resultSet.getString("operation")))
                .entityId(resultSet.getLong("entity_id"))
                .build();
    }

    private Map<String, Object> feedToMap(Event event) {
        Map<String, Object> values = new HashMap<>();
        values.put("event_id", event.getEventId());
        values.put("event_time", event.getTimestamp());
        values.put("user_id", event.getUserId());
        values.put("event_type", event.getEventType());
        values.put("operation", event.getOperation());
        values.put("entity_id", event.getEntityId());
        return values;
    }
}
