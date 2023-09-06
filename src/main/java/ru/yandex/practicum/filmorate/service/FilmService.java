package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.interfaces.EventStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
                eventStorage.add(userId, filmId, EventTypes.LIKE, EventOperations.ADD);
            } else {
                done = likeStorage.unlike(filmId, userId);
                eventStorage.add(userId, filmId, EventTypes.LIKE, EventOperations.REMOVE);
            }
        }
        return done;
    }

    public Film findById(Long id) {
        return filmStorage.findById(id);
    }

    public boolean checkById(long id) {
        return filmStorage.checkById(id);
    }

    public Film update(Film film) {
        removeDuplicateDirectors(film);
        return filmStorage.update(film);
    }

    private void removeDuplicateDirectors(Film film) {
        List<Director> uniqueDirectors = film.getDirectors().stream()
                .distinct()
                .collect(Collectors.toList());
        film.setDirectors(uniqueDirectors);
    }

    public Film add(Film film) {
        removeDuplicateDirectors(film);
        return filmStorage.add(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        return filmStorage.getPopularFilm(count, genreId, year);
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
        Long similarUserId = userService.getSimilarId(id);
        if (similarUserId == null) {
            return Collections.EMPTY_LIST;
        }
        List<Film> likedByUser = filmStorage.getLikedFilms(id);
        List<Film> likedBySimilar = filmStorage.getLikedFilms(similarUserId);
        likedBySimilar.removeAll(likedByUser);
        return likedBySimilar;
    }

    public List<Film> getFilmsByDirectorSorted(int directorId, SortBy sortBy) {
        return filmStorage.getFilmsByDirectorSorted(directorId, sortBy);
    }

    public List<Film> searchByFilmAndDirectorSorted(String query, SearchBy searchBy) {
        return filmStorage.searchByFilmAndDirectorSorted(query, searchBy);
    }
}
