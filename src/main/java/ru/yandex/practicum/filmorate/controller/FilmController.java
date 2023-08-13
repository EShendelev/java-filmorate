package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    private final String URI = "/films";
    private final String NO_BODY = "no body";

    @GetMapping
    public Collection<Film> findAll() {
        Logger.logRequest(HttpMethod.GET, URI, NO_BODY);
        Logger.logInfo("Получен весь список фильмов");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Long id) {
        Logger.logRequest(HttpMethod.GET, URI + "/" + id, NO_BODY);
        Film findedFilm = filmService.findById(id);
        Logger.logInfo(String.format("Фильм id %d найден", id));
        return findedFilm;
    }

    @GetMapping("/popular")
    public Collection<Film> getListOfPopularFilms(@RequestParam(defaultValue = "10") @Positive Integer count) {
        Logger.logRequest(HttpMethod.GET, URI + "/popular?count=" + count, NO_BODY);
        Logger.logInfo("Показан список популярных фильмов");
        return filmService.findPopularFilms(count);
    }

    @GetMapping("/{id}/likes")
    public List<Long> getListOfLikes(@PathVariable long id) {
        Logger.logRequest(HttpMethod.GET, URI + "/" + id + "/likes", NO_BODY);
        Logger.logInfo("Получен список id пользователей, поставивших лайк");
        return filmService.getListOfLikes(id);
    }

    @PostMapping
    public Film addFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Film crFilm = filmService.add(film);
            Logger.logRequest(HttpMethod.POST, URI, film.toString());
            Logger.logInfo(String.format("Фильм \"%s\" добавлен", crFilm.getName()));
            return crFilm;
        }
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody @Valid Film film) {
        if (validateFilm(film)) {
            Logger.logRequest(HttpMethod.PUT, URI, film.toString());
            Film upFilm = filmService.update(film);
            Logger.logInfo(String.format("Фильм id %s обновлен", film.getId()));
            return upFilm;
        }
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) {
        Logger.logRequest(HttpMethod.PUT, URI + "/" + id + "/like/" + userId, NO_BODY);
        filmService.doLike(id, userId, true);
        Logger.logInfo(String.format("Пользователь с id %d поставил лайк фильму с id %d", userId, id));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable Long id, @PathVariable Long userId) {
        Logger.logRequest(HttpMethod.DELETE, URI + "/" + id + "/like/" + userId, NO_BODY);
        filmService.doLike(id, userId, false);
        Logger.logInfo(String.format("Пользователь с id %d удалил лайк фильму с id %d", userId, id));
    }

    boolean validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new FilmValidateFailException("Дата релиза не может быть ранее 28.12.1985 г.");
        }
        return true;
    }
}
