package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Collection<Film> findAll();

    Film add(Film film);

    Film update(Film film);

    Film findById(Long id);

    boolean checkById(long id);

    List<Film> getFilmsByDirectorSorted(int directorId, String sortBy);

    List<Film> searchByFilmAndDirectorSorted(String query, String by);

    Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year);

    Collection<Film> getCommonFilms(Integer id, Integer friendId);

    void deleteFilmById(long id);
}