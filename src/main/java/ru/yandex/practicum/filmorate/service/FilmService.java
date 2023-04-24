package ru.yandex.practicum.filmorate.service;


import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.FilmValidateFailException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


import ru.yandex.practicum.filmorate.service.utils.FilmIdProvider;


@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    static final LocalDate MIN_DATE = LocalDate.of(1895, 12, 28);
    static final int MAX_LEN = 200;

    public Film addFilm(Film film) {
        int id = FilmIdProvider.getIncrementId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmNotExistException(String.format("Фильм с  id %d не найден", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    public Film findFilmById(int id) {
        if (!films.containsKey(id)) {
            throw new FilmNotExistException(String.format("Фильм id %d не найден", id));
        }
        return films.get(id);
    }

    public boolean validateFilm(Film film) {

        if (film.getName() == null || film.getName().isBlank()) {
            throw new FilmValidateFailException("Название не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LEN) {
            throw new FilmValidateFailException("Максимальная длина описания - 200 символов.");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE)) {
            throw new FilmValidateFailException("Дата релиза не может быть ранее 28.12.1985 г.");
        }
        return true;
    }
}
