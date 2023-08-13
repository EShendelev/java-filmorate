package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;
    private final FilmGenreStorage filmGenreStorage;

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    //getListOfGenres выводит список жанров фильма по его id
    public List<Genre> getListOfGenres(long id) {
        return filmGenreStorage.getListOfGenres(id).stream().map(genreStorage::getGenreById)
                .collect(Collectors.toList());
    }

    public Collection<Genre> getGenres() {
        return genreStorage.getGenres();
    }

}
