package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @GetMapping
    public Collection<Film> findAll() {
        log.debug("find all films");
        return filmService.findAll();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        Film addedFilm = filmService.addFilm(film);
        log.debug("film added");
        return addedFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film updFilm = filmService.updateFilm(film);
        log.debug("film updated");
        return updFilm;
    }

    @GetMapping("/film/{id}")
    public Film getFilm(@PathVariable("id") int id) {
        Film findedFilm = null;
        try {
            findedFilm = filmService.findFilmById(id);
        } catch (FilmNotExistException e) {
            log.debug(e.getMessage());
        }
        log.debug("film №" + id + " finded");
        return findedFilm;
    }

}
