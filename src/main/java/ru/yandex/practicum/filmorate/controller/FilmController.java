package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;


import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")

public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;
    static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    static final int MAX_LEN = 200;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("find all films");
        return filmStorage.findAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film crFilm = filmStorage.add(film);
            log.info("film {} id {} added", film.getName(), film.getId());
            return crFilm;
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film upFilm = filmStorage.update(film);
            log.info("film id {} updated", film.getId());
            return upFilm;
        }
        return film;
    }

    @GetMapping("/film/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        Film findedFilm = filmStorage.findById(id);
        log.info("film № {} found", id);
        return findedFilm;
    }

    boolean validateFilm(Film film) {
        if (film.getDescription().length() > MAX_LEN) {
            throw new FilmValidateFailException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new FilmValidateFailException("Дата релиза не может быть ранее 28.12.1985 г.");
        }
        return true;
    }

}
