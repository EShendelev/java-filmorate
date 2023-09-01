package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {
    Event add(long userId, long entityId, String event, String operation);

    List<Event> getUserEvents(long userId);

    Event getFeedById(long id);
}
