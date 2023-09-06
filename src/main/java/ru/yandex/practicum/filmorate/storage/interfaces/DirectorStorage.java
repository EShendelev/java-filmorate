package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Map;

public interface DirectorStorage {
    List<Director> getDirectors();

    List<Director> getDirectorsByFilmId(long filmId);

    Map<Long, List<Director>> getDirectorsByFilmIds(List<Long> filmIds);

    Director getDirectorById(int directorId);

    Director addDirector(Director director);

    void addDirectors(List<Director> genres, long filmId);

    Director updateDirector(Director director);


    void deleteDirector(int directorId);

    void deleteDirectorsFromFilm(long filmId);

    boolean checkById(int id);
}
