package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class FilmService {

    FilmStorage filmStorage;

    @Autowired
    FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;

    }

    public Film doLike(Long filmId, Long userId, boolean like) {
        Film film = filmStorage.findById(filmId);

        Set<Long> rate = film.getLikes();
        if (userId <= 0) {
            throw new UserNotExistException(String.format("Пользователь с id %d не существует", userId));
        }
        if (like) {
            rate.add(userId);
        } else {
            rate.remove(userId);
        }

        return film;
    }

    public Collection<Film> findPopularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted((p0, p1) -> {
                    int comp = p0.getLikesCount().compareTo(p1.getLikesCount());
                    return -1 * comp;
                })
                .limit(count).collect(Collectors.toList());
    }

    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }
}
