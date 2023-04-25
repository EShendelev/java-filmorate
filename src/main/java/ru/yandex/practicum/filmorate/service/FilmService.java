package ru.yandex.practicum.filmorate.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.utils.FilmIdProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();

    public Film addFilm(Film film) {
        int id = FilmIdProvider.getIncrementId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException(String.format("Фильм с  id %d не найден", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film findFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotExistException(String.format("Фильм id %d не найден", id));
        }
        return films.get(id);
    }

}
