package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreStorage {
    void addGenres(List<Genre> genres, long filmId);

    void deleteGenres(long filmId);

    List<Integer> getListOfGenres(long id);
}
