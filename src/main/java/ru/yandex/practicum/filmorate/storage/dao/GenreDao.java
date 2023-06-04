package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;

public class GenreDao  implements GenreStorage {
    @Override
    public Collection<Genre> getGenres() {
        return null;
    }

    @Override
    public Genre getGenreById(int genreId) {
        return null;
    }
}
