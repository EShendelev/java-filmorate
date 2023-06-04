package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.Collection;

public interface MpaRatingStorage {
    Collection<MpaRating> getMpa();
    MpaRating getMpaById(int mpaId);
}
