package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.utils.FilmIdProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    FilmIdProvider idProvider;
    protected final Map<Long, Film> films;

    public InMemoryFilmStorage() {
        idProvider = new FilmIdProvider();
        films = new HashMap<>();
    }

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film add(Film film) {
        Long id = idProvider.getIncrementId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException(String.format("Фильм с  id %d не найден", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film delete(Long id) {
        return null;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotExistException(String.format("Фильм id %d не найден", id));
        }
        return films.get(id);
    }
}
