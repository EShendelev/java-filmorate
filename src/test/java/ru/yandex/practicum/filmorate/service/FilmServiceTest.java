package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmServiceTest {

    FilmService filmService;

    @BeforeEach
    void beforeEach() {
        filmService = new FilmService();
    }

    @Test
    void validateFilmWithEmptyName() {
        Film film = new Film(1, "", "description", LocalDate.now(), 1);
        assertThrows(FilmValidateFailException.class, () -> {
            filmService.validateFilm(film);
        });
    }

    @Test
    void validateFilmWithDescriptionOverMaxLength() {
        Film film = new Film(1, "", "description", LocalDate.now(), 1);
        film.setDescription("a".repeat(201));
        assertThrows(FilmValidateFailException.class, () -> {
            filmService.validateFilm(film);
        });
    }

    @Test
    void validateFilmWithDateBeforeFilmsBirthday() {
        Film film = new Film(1, "", "description", LocalDate.of(1984, 12, 15), 1);
        film.setDescription("a".repeat(201));
        assertThrows(FilmValidateFailException.class, () -> {
            filmService.validateFilm(film);
        });
    }
}