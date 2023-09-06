package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface MpaRatingStorage {
    Collection<Mpa> getMpa();

    Mpa getMpaById(int mpaId);

    Map<Integer, Mpa> getMpaRatingByMpaIds(List<Integer> mpaIds);
}
