package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film add(Film film);

    Film update(Film film);

    Film delete(Long id);

    Film findById(Long id);
}