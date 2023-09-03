package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventTypes;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.EventStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final UserService userService;
    private final EventStorage eventStorage;

    public boolean doLike(Long filmId, Long userId, boolean like) {
        boolean checkFilm = filmStorage.checkById(filmId);
        boolean checkUser = userService.checkById(userId);
        boolean done = false;
        if (checkFilm && checkUser) {
            if (like) {
                done = likeStorage.addLike(filmId, userId);
                eventStorage.add(userId, filmId, EventTypes.LIKE.toString(),EventOperations.ADD.name());
            } else {
                done = likeStorage.unlike(filmId, userId);
                eventStorage.add(userId, filmId, EventTypes.LIKE.toString(),EventOperations.REMOVE.name());
            }
        }
        return done;
    }
/*
    public Collection<Film> findPopularFilms(Integer count) {
        return filmStorage.findAll().stream()
                .sorted((p0, p1) -> {
                    int comp = p0.getLikesCount().compareTo(p1.getLikesCount());
                    return -1 * comp;
                })
                .limit(count).collect(Collectors.toList());
    }

 */



    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public boolean checkById(long id) {
        return filmStorage.checkById(id);
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

    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        if (genreId == null && year == null) {
            return filmStorage.findAll().stream()
                    .sorted((p0, p1) -> {
                        int comp = p0.getLikesCount().compareTo(p1.getLikesCount());
                        return -1 * comp;
                    })
                    .limit(count).collect(Collectors.toList());
        }
        return filmStorage.getPopularFilm(genreId, year).stream()
                .sorted((p0, p1) -> {
                    int comp = p0.getLikesCount().compareTo(p1.getLikesCount());
                    return -1 * comp;
                })
                .limit(count).collect(Collectors.toList());
    }

    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        return filmStorage.getCommonFilms(userId, friendId).stream()
                .sorted((p0, p1) -> {
                    int comp = p0.getLikesCount().compareTo(p1.getLikesCount());
                    return -1 * comp;
                })
                .collect(Collectors.toList());
    }

    public List<Long> getListOfLikes(long id) {
        List<Long> films = new ArrayList<>();
        if (filmStorage.checkById(id)) {
            films = likeStorage.getLikesList(id);
        }
        return films;
    }

    public void deleteFilmById(long id) {
        filmStorage.deleteFilmById(id);
    }

    public List<Film> getRecommendations(long id) {
        return filmStorage.getRecommendations(id);
    }
}
