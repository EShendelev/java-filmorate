package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaRatingStorage {
    Collection<Mpa> getMpa();

    Mpa getMpaById(int mpaId);
}
