package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;
    private final FilmStorage filmStorage;

    public Director getDirectorById(int id) {
        return directorStorage.getDirectorById(id);
    }

    public Collection<Director> getDirectorsByFilmId(long filmId) {
        return directorStorage.getDirectorsByFilmId(filmId);
    }

    public Collection<Director> getDirectors() {
        return directorStorage.getDirectors();
    }

    public Director addDirector(Director director) {
        return directorStorage.addDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorStorage.updateDirector(director);
    }

    public void deleteDirector(int directorId) {
        directorStorage.deleteDirector(directorId);
    }

    public void deleteDirectorsFromFilm(long filmId) {
        directorStorage.deleteDirectorsFromFilm(filmId);
    }

    public List<Film> getFilmsByDirectorSorted(int directorId, String sortBy) {
        return filmStorage.getFilmsByDirectorSorted(directorId, sortBy);
    }
}
