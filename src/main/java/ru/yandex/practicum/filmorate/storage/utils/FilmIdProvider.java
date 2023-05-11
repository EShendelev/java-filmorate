package ru.yandex.practicum.filmorate.storage.utils;

import org.springframework.stereotype.Component;

@Component
public class FilmIdProvider {
    private Long id = 0L;

    public Long getIncrementId() {
        return ++id;
    }
}
