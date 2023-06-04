package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;

import java.util.List;

public class FilmGenreDao implements FilmGenreStorage {

    @Override
    public void addGenres(List<Genre> genres, long filmId) {

    }

    @Override
    public void deleteGenres(long filmId) {

    }

    @Override
    public List<Integer> getListOfGenres(long id) {
        return null;
    }
}
