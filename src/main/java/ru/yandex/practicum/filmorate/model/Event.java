package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {
    private long id;
    private long timeStamp;
    private long userId;
    private long entityId;
    private String eventType;
    private String operation;
}
