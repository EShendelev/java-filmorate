package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getDirectors();

    List<Director> getDirectorsByFilmId(long filmId);

    Director getDirectorById(int directorId);

    Director addDirector(Director director);

    void addDirectors(List<Director> genres, long filmId);

    Director updateDirector(Director director);


    void deleteDirector(int directorId);

    void deleteDirectorsFromFilm(long filmId);

    boolean checkById(int id);
}
