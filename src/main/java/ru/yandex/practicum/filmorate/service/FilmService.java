package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserService userService;


    public boolean doLike(Long filmId, Long userId, boolean like) {
        filmStorage.findById(filmId);
        userService.findById(userId);
        boolean done = false;
        if (userId <= 0) {
            throw new UserNotExistException(String.format("Пользователь с id %d не существует", userId));
        }
        if (like) {
            done = likeStorage.addLike(filmId, userId);
        } else {
            done = likeStorage.unlike(filmId, userId);
        }

        return done;
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

    public List<Long> getListOfLikes(long id) {
        filmStorage.findById(id);
        return likeStorage.getLikesList(id);
    }
}
