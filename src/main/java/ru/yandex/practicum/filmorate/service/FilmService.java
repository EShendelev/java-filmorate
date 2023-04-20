package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class FilmService {
    private final Map<Integer, Film> films = new HashMap<>();
    public Film addFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    public Collection<Film> findAll() {
        Film film = new Film(1, "", "", LocalDateTime.now(), Duration.ofMinutes(234));
        films.put(1, film);
        return films.values();
    }

    public Film findFilmById(int id){
        return films.get(id);
    }
}
