package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final DirectorService directorService;
    static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private static final String URI = "/films";
    private static final String NOBODY = "no body";

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
    public Collection<Film> getListOfPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count) {
        log.info("Показан список популярных фильмов");
        return filmService.findPopularFilms(count);
    }

    @GetMapping("/{id}/likes")
    public List<Long> getListOfLikes(@PathVariable long id) {
        log.info("Получен список id пользователей, поставивших лайк");
        return filmService.getListOfLikes(id);
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film crFilm = filmService.add(film);
            log.info(String.format("Фильм \"%s\" добавлен", crFilm.getName()));
            return crFilm;
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film upFilm = filmService.update(film);
            log.info(String.format("Фильм id %s обновлен", film.getId()));
            return upFilm;
        }
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.doLike(id, userId, true);
        log.info(String.format("Пользователь с id %d поставил лайк фильму с id %d", userId, id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        filmService.doLike(id, userId, false);
        log.info(String.format("Пользователь с id %d удалил лайк фильму с id %d", userId, id));
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirectorSorted(@PathVariable int directorId, @RequestParam String sortBy) {
        return directorService.getFilmsByDirectorSorted(directorId, sortBy);
    }

    boolean validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new FilmValidateFailException("Дата релиза не может быть ранее 28.12.1985 г.");
        }
        return true;
    }
}
