package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.utils.FilmIdProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    FilmIdProvider idProvider;
    protected final Map<Long, Film> films = new HashMap<>();

    public InMemoryFilmStorage(FilmIdProvider idProvider) {
        this.idProvider = idProvider;
    }

    @Override
    public Film add(Film film) {
        Long id = idProvider.getIncrementId();
        film.setId(id);
        films.put(id, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Long id = film.getId();
        if (!films.containsKey(id)) {
            throw new FilmNotExistException(String.format("Ошибка обновления данных. " +
                    "Фильм с  id %d не найден", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film findById(Long id) {
        if (!films.containsKey(id)) {
            throw new FilmNotExistException(String.format("Ошибка поиска. Фильм id %d не найден", id));
        }
        return films.get(id);
    }

    //заглушка для работы с внешней БД
    @Override
    public boolean checkById(long id) {
        return false;
    }

    @Override
    public void deleteFilmById(long id) {
        films.remove(id);
    }

    @Override
    public List<Film> getFilmsByDirectorSorted(int directorId, String sortBy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Film> searchByFilmAndDirectorSorted(String query, String by) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Film> getCommonFilms(Integer id, Integer friendId) {
        throw new UnsupportedOperationException();
    }

}
