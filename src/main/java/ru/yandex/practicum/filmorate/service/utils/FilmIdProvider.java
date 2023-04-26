package ru.yandex.practicum.filmorate.service.utils;

public class FilmIdProvider {
    private static int id = 0;

    public static int getIncrementId() {
        return ++id;
    }
}
