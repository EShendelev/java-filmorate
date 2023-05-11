package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    FilmController filmController;
    FilmService filmService;
    FilmStorage filmStorage;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController(filmService);
    }

    @Test
    void validateFilmWithDescriptionOverMaxLength() {

        Film film = new Film(1L, "", "description", LocalDate.now(), 1,
                new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L)));
        film.setDescription("a".repeat(201));
        assertThrows(FilmValidateFailException.class, () -> {
            filmController.validateFilm(film);
        });
    }

    @Test
    void validateFilmWithDateBeforeFilmsBirthday() {
        Film film = new Film(1L, "", "description", LocalDate.of(1984, 12, 15),
                1, new HashSet<Long>(List.of(1L, 2L, 3L, 4L, 5L)));
        film.setDescription("a".repeat(201));
        assertThrows(FilmValidateFailException.class, () -> {
            filmController.validateFilm(film);
        });
    }
}