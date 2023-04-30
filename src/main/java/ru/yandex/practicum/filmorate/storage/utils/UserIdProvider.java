package ru.yandex.practicum.filmorate.storage.utils;

public class UserIdProvider {
    private static Long id = 0L;

    public static Long getIncrementId() {
        return ++id;
    }
}
