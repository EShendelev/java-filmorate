package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public Collection<Film> findAll() {
        log.info("find all films");
        return filmService.findAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (filmService.validateFilm(film)) {
            Film crFilm = filmService.addFilm(film);
            log.info(String.format("film %s id %d added", film.getName(), film.getId()));
            return crFilm;
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (filmService.validateFilm(film)) {
            Film upFilm = filmService.updateFilm(film);
            log.info(String.format("film id %d updated", film.getId()));
            return upFilm;
        }
        return film;
    }

    @GetMapping("/film/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        Film findedFilm = filmService.findFilmById(id);
        log.info("film №" + id + " found");
        return findedFilm;
    }

}
