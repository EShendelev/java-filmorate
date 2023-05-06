package ru.yandex.practicum.filmorate.storage.utils;

public class UserIdProvider {
    private Long id = 0L;

    public Long getIncrementId() {
        return ++id;
    }
}
