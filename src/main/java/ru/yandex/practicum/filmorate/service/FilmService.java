package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import javax.validation.constraints.NotNull;
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


    public void doLike(Long filmId, Long userId, @NotNull boolean like) {
        Film film = filmStorage.findById(filmId);
        ;
        Set<Long> rate = film.getLikes();
        if (userId <= 0) {
            throw new UserNotExistException(String.format("Пользователь с id %d не существует", userId));
        }
        if (like) {
            rate.add(userId);
        } else {
            rate.remove(userId);
        }
    }

    public Collection<Film> findPopularFilms(Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }

        return filmStorage.findAll().stream()
                .sorted((p0, p1) -> {
                    int comp = p0.getLikesCount().compareTo(p1.getLikesCount());
                    return -1 * comp;
                })
                .limit(count).collect(Collectors.toList());
    }
}
