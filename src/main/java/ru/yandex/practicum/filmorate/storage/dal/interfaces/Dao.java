package ru.yandex.practicum.filmorate.storage.dal.interfaces;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
    Optional<T> get(Long id);
    List<T> getAll();
    void save(T t);
    void update(Long id, String[] params);
    void delete(Long id);

}
