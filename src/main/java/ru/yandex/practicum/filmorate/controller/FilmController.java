package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    static final int MAX_LEN = 200;

    public Fроцесс (javaw.exilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен весь список фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long id) {
        Film findedFilm = filmService.findById(id);
        log.info("Фильм id {} найден", id);
        return findedFilm;
    }

    @GetMapping("/popular")
    public Collection<Film> getListOfPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Показаны {} популярных фильмов", count);
        return filmService.findPopularFilms(count);
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film crFilm = filmService.add(film);
            log.info("Фильм {} id  {} добавлен", film.getName(), film.getId());
            return crFilm;
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film upFilm = filmService.update(film);
            log.info("Фильм id {} обновлен", film.getId());
            return upFilm;
        }
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        Film film = filmService.doLike(id, userId, true);
        log.info("Фильм id {} получил лайк от пользователя id {}", id, userId);
        return film;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        Film film = filmService.doLike(id, userId, false);
        log.info("Пользователь id {} удалил свой лайк фильму id {}", userId, id);
        return film;
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
