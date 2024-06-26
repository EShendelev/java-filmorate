package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SearchBy;
import ru.yandex.practicum.filmorate.model.SortBy;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
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

    @GetMapping("/{id}/likes")
    public List<Long> getListOfLikes(@PathVariable long id) {
        log.info("Получен список id пользователей, поставивших лайк");
        return filmService.getListOfLikes(id);
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film crFilm = filmService.add(film);
            log.info(String.format("Фильм \"%s\" добавлен, c id = \"%s\"", crFilm.getName(), crFilm.getId()));
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
        SortBy sortByEnum = SortBy.valueOf(sortBy.toUpperCase());
        return filmService.getFilmsByDirectorSorted(directorId, sortByEnum);
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilmById(@PathVariable long filmId) {
        filmService.deleteFilmById(filmId);
        log.info(String.format("Фильм с id=%d удален", filmId));
    }

    @GetMapping("/search")
    public List<Film> searchByFilmAndDirectorSorted(@RequestParam String query, @RequestParam("by") String searchBy) {
        SearchBy searchByEnum;
        if ("director".equals(searchBy)) {
            searchByEnum = SearchBy.DIRECTOR;
        } else if ("title".equals(searchBy)) {
            searchByEnum = SearchBy.TITLE;
        } else if ("director,title".equals(searchBy) || "title,director".equals(searchBy)) {
            searchByEnum = SearchBy.TITLEANDDIRECTOR;
        } else {
            throw new IncorrectParameterException("Неверно указаны параметры поиска");
        }
        return filmService.searchByFilmAndDirectorSorted(query, searchByEnum);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilm(@RequestParam(name = "count", defaultValue = "10",
            required = false) Integer count,
                                           @RequestParam(name = "genreId", required = false) Integer genreId,
                                           @RequestParam(name = "year", required = false) Integer year) {
        return filmService.getPopularFilm(count, genreId, year);
    }

    @GetMapping("/common")
    public Collection<Film> getCommonFilms(@RequestParam(name = "userId") Integer userId,
                                           @RequestParam(name = "friendId") Integer friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    boolean validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new FilmValidateFailException("Дата релиза не может быть ранее 28.12.1985 г.");
        }
        return true;
    }
}
