package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
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
    public Event add(long userId, long entityId, String event, String operation) {
        Event feed = Event.builder()
                .timeStamp(Timestamp.valueOf(LocalDateTime.now()).getTime())
                .userId(userId)
                .entityId(entityId)
                .eventType(event)
                .operation(operation)
                .build();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("id");
        long feedId = simpleJdbcInsert.executeAndReturnKey(feedToMap(feed)).longValue();
        return getFeedById(feedId);
    }

    @Override
    public List<Event> getUserEvents(long userId) {
        String sqlQuery = "SELECT * FROM events WHERE user_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFeed, userId);
    }

    @Override
    public Event getFeedById(long eventId) {
        String sqlQuery = "SELECT * FROM events WHERE id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFeed, eventId);
    }

    private Event mapRowToFeed(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .id(resultSet.getLong("id"))
                .timeStamp(resultSet.getLong("event_time"))
                .userId(resultSet.getLong("user_id"))
                .eventType(resultSet.getString("event_type"))
                .operation(resultSet.getString("operation"))
                .entityId(resultSet.getLong("entity_id"))
                .build();
    }

    private Map<String, Object> feedToMap(Event event) {
        Map<String, Object> values = new HashMap<>();
        values.put("id", event.getId());
        values.put("event_time", event.getTimeStamp());
        values.put("user_id", event.getUserId());
        values.put("event_type", event.getEventType());
        values.put("operation", event.getOperation());
        values.put("entity_id", event.getEntityId());
        return values;
    }
}
