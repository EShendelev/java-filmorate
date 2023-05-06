package ru.yandex.practicum.filmorate.storage.utils;

public class FilmIdProvider {
    private Long id = 0L;

    public Long getIncrementId() {
        return ++id;
    }
}
