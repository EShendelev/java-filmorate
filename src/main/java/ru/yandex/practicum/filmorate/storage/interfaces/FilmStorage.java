package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Map<Long, Film> getFilms();

    Collection<Film> findAll();

    Film add(Film film);

    Film update(Film film);

    Film delete(Long id);

    Film findById(Long id);
}
