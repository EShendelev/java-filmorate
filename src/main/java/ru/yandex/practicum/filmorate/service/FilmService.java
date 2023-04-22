package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.service.utils.FilmIdProvider;


@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    public Film addFilm(Film film) {
        int id = FilmIdProvider.getIncrementId();
        film.setId(id);
        try {
            validateFilm(film);
        } catch (FilmValidateFailException e) {
            log.debug(e.getMessage());
        }
        films.put(id, film);
        return film;
    }

    public Film updateFilm(Film film) {
        try {
            validateFilm(film);
        } catch (FilmValidateFailException e) {
            log.debug(e.getMessage());
        }
        films.put(film.getId(), film);
        return film;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film findFilmById(int id) throws FilmNotExistException {
        if (!films.containsKey(id)) {
            throw new FilmNotExistException("Фильм не найден");
        }
        return films.get(id);
    }

    private void validateFilm(Film film) throws FilmValidateFailException {
        final LocalDate minDate = LocalDate.of(1895, 12, 28);
        final int maxLen = 200;
        if (film.getName().isBlank()) {
            throw new FilmValidateFailException("Название не может быть пустым");
        }
        if (film.getDescription().length() > maxLen) {
            throw new FilmValidateFailException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate().isBefore(minDate)) {
            throw new FilmValidateFailException("Дата релиза не может быть ранее 28.12.1985 г.");
        }
        if (film.getDuration().isNegative()) {
            throw new FilmValidateFailException("Продолжительность фильма должна быть положительной");
        }
    }
}
