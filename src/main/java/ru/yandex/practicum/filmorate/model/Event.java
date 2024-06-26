package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private long eventId;
    private long timestamp;
    private long userId;
    private long entityId;
    private EventTypes eventType;
    private EventOperations operation;
}

