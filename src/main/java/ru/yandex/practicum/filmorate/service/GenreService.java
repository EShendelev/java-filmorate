package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;
    private final FilmGenreStorage filmGenreStorage;

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getListOfGenres(long filmId) {
        return genreStorage.getFilmGenres(filmId);
    }

    public Collection<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Map<Long, List<Genre>> getGenresByIds(List<Long> filmIds) {
        return genreStorage.getGenresByIds(filmIds);
    }

}
