package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmControllerTest {

    FilmController filmController;
    FilmService filmService;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController(filmService);
    }

    @Test
    void validateFilmWithDescriptionOverMaxLength() {
        Film film = new Film(1, "", "description", LocalDate.now(), 1);
        film.setDescription("a".repeat(201));
        assertThrows(FilmValidateFailException.class, () -> {
            filmController.validateFilm(film);
        });
    }

    @Test
    void validateFilmWithDateBeforeFilmsBirthday() {
        Film film = new Film(1, "", "description", LocalDate.of(1984, 12, 15), 1);
        film.setDescription("a".repeat(201));
        assertThrows(FilmValidateFailException.class, () -> {
            filmController.validateFilm(film);
        });
    }
}