package ru.yandex.practicum.filmorate.storage.utils;

import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;


public class Storages {
    public static UserStorage getDefaultUserStorage() {
        return new InMemoryUserStorage();
    }

    public static FilmStorage getDefaultFilmStorage() {
        return new InMemoryFilmStorage();
    }
}
