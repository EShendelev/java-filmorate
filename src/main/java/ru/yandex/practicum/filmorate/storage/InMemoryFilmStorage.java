package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.utils.FilmIdProvider;

import java.util.*;

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
        film.setLikes(new HashSet<>());
        return film;
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();
        if (!films.containsKey(id)) {
            throw new FilmNotExistException(String.format("Фильм с  id %d не найден", film.getId()));
        }
        Set<Long> filmLikes = films.get(id).getLikes();
        if (filmLikes == null) {
            film.setLikes(new HashSet<>());
        }
        film.setLikes(filmLikes);
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
