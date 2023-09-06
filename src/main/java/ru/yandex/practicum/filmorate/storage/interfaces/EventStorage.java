package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventTypes;

import java.util.List;

public interface EventStorage {
    Event add(long userId, long entityId, EventTypes event, EventOperations operation);

    List<Event> getUserEvents(long userId);

    Event getFeedById(long id);
}
