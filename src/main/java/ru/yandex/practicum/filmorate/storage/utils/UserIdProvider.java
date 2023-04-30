package ru.yandex.practicum.filmorate.storage.utils;

public class UserIdProvider {
    private static int id = 0;

    public static int getIncrementId() {
        return ++id;
    }
}
