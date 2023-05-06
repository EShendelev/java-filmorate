package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.utils.Storages;

import java.util.Collection;
import java.util.stream.Collectors;


@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;


    @Autowired
    FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = Storages.getDefaultFilmStorage();
        this.userStorage = Storages.getDefaultUserStorage();
    }


    private Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.findById(filmId);
        User user = userStorage.findById(userId);


        return null;
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
