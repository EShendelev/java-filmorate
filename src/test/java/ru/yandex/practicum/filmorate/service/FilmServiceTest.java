package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.utils.Storages;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmServiceTest {
    private UserStorage userStorage;
    private FilmStorage filmStorage;
    private FilmService filmService;


    @BeforeEach
    void beforeEach() {
        userStorage = Storages.getDefaultUserStorage();
        filmStorage = Storages.getDefaultFilmStorage();
        filmService = new FilmService(filmStorage);
    }

    @Test
    void findPopularFilmsTest() {
        Film film1 = new Film(1L, "1", "description1", LocalDate.now(), 1,
                new HashSet<Long>(List.of(1L, 2L)));
        Film film2 = new Film(2L, "2", "description2", LocalDate.now().plusDays(1),
                1, new HashSet<Long>(List.of(1L, 2L, 3L)));
        Film film3 = new Film(3L, "3", "description3", LocalDate.now().plusDays(2),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L)));
        Film film4 = new Film(4L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L)));

        filmStorage.add(film1);
        filmStorage.add(film2);
        filmStorage.add(film3);
        filmStorage.add(film4);

        Collection<Film> sortedFilm = filmService.findPopularFilms(4);
        Long[] sortedId = sortedFilm.stream().map(Film::getId).toArray(Long[]::new);
        assertEquals(4, sortedFilm.size());
        assertArrayEquals(sortedId, new Long[] {4L, 3L, 2L, 1L});
    }

    @Test
    void findPopularFilmsWithCountZeroTest() {
        Film film1 = new Film(1L, "1", "description1", LocalDate.now(), 1,
                new HashSet<Long>(List.of(1L)));
        Film film2 = new Film(2L, "2", "description2", LocalDate.now().plusDays(1),
                1, new HashSet<Long>(List.of(1L, 2L)));
        Film film3 = new Film(3L, "3", "description3", LocalDate.now().plusDays(2),
                1, new HashSet<Long>(List.of(1L, 2L, 3L)));
        Film film4 = new Film(4L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L)));
        Film film5 = new Film(5L, "1", "description1", LocalDate.now(), 1,
                new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L)));
        Film film6 = new Film(6L, "2", "description2", LocalDate.now().plusDays(1),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L)));
        Film film7 = new Film(7L, "3", "description3", LocalDate.now().plusDays(2),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)));
        Film film8 = new Film(8L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)));
        Film film9 = new Film(9L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)));
        Film film10 = new Film(10L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)));
        Film film11 = new Film(11L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L)));

        filmStorage.add(film1);
        filmStorage.add(film2);
        filmStorage.add(film3);
        filmStorage.add(film4);
        filmStorage.add(film5);
        filmStorage.add(film6);
        filmStorage.add(film7);
        filmStorage.add(film8);
        filmStorage.add(film9);
        filmStorage.add(film10);
        filmStorage.add(film11);

        Collection<Film> sortedFilm = filmService.findPopularFilms(0);
        Object[] sortedId = sortedFilm.stream().map(Film::getId).toArray();
        assertEquals(10, sortedFilm.size());
        assertArrayEquals(new Object[] {11L, 10L, 9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L}, sortedId);
    }

    @Test
    void findPopularFilmsWithCountNullTest() {
        Film film1 = new Film(1L, "1", "description1", LocalDate.now(), 1,
                new HashSet<Long>(List.of(1L)));
        Film film2 = new Film(2L, "2", "description2", LocalDate.now().plusDays(1),
                1, new HashSet<Long>(List.of(1L, 2L)));
        Film film3 = new Film(3L, "3", "description3", LocalDate.now().plusDays(2),
                1, new HashSet<Long>(List.of(1L, 2L, 3L)));
        Film film4 = new Film(4L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L)));
        Film film5 = new Film(5L, "1", "description1", LocalDate.now(), 1,
                new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L)));
        Film film6 = new Film(6L, "2", "description2", LocalDate.now().plusDays(1),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L)));
        Film film7 = new Film(7L, "3", "description3", LocalDate.now().plusDays(2),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L)));
        Film film8 = new Film(8L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L)));
        Film film9 = new Film(9L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L)));
        Film film10 = new Film(10L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)));
        Film film11 = new Film(11L, "4", "description4", LocalDate.now().plusDays(3),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L)));

        filmStorage.add(film1);
        filmStorage.add(film2);
        filmStorage.add(film3);
        filmStorage.add(film4);
        filmStorage.add(film5);
        filmStorage.add(film6);
        filmStorage.add(film7);
        filmStorage.add(film8);
        filmStorage.add(film9);
        filmStorage.add(film10);
        filmStorage.add(film11);


        Collection<Film> sortedFilm = filmService.findPopularFilms(null);
        Object[] sortedId = sortedFilm.stream().map(Film::getId).toArray();
        assertEquals(10, sortedFilm.size());
        assertArrayEquals(new Object[] {11L, 10L, 9L, 8L, 7L, 6L, 5L, 4L, 3L, 2L}, sortedId);
    }

    @Test
    void addLikeTest() {
        User user = new User(1L, "email@emal.ru", "Login", "Name",
                LocalDate.now().plusDays(2), new HashSet<>());
        Film film = new Film(1L, "1", "description1", LocalDate.now(), 1,
                new HashSet<Long>(List.of(2L)));
        filmStorage.add(film);
        userStorage.add(user);
        filmService.doLike(1L, 1L, true);
        assertArrayEquals(new Object[] {1L, 2L}, film.getLikes().toArray());
    }

    @Test
    void deleteLikeTest() {
        User user = new User(1L, "email@emal.ru", "Login", "Name",
                LocalDate.now().plusDays(2), new HashSet<>());
        Film film = new Film(1L, "1", "description1", LocalDate.now(), 1,
                new HashSet<Long>(List.of(1L, 2L)));
        filmStorage.add(film);
        userStorage.add(user);
        filmService.doLike(1L, 1L, false);
        assertArrayEquals(new Object[] {2L}, film.getLikes().toArray());
    }
}