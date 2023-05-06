package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;


    @Autowired
    FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public void doLike(Long filmId, Long userId, @NotNull boolean like) {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);
        Set<Long> likes = film.getLikes();
        Set<Long> userFilms = user.getLikedFilms();
        if (like) {
            likes.add(userId);
            userFilms.add(filmId);
        } else {
            likes.remove(userId);
            userFilms.remove(filmId);
        }
    }

    private Film deleteLike(Film film) {

        return film;
    }

    public Collection<Film> findPopularFilms(Collection<Film> films, Integer count) {
        if (count == null || count == 0) {
            count = 10;
        }
        return films.stream()
                .sorted((p0, p1) -> {
                    int comp = p0.getLikesCount().compareTo(p1.getLikesCount());
                    return -1 * comp;
                })
                .limit(count).collect(Collectors.toList());
    }
}
