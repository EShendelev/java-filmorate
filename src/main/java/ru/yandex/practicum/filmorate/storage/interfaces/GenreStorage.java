package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    Collection<Genre> getGenres();

    Genre getGenreById(int genreId);

    List<Genre> getFilmGenres(long filmId);
}
