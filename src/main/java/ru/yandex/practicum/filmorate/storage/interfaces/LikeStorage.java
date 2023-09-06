package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;
import java.util.Map;

public interface LikeStorage {
    boolean addLike(long filmId, long userId);

    boolean unlike(long filmId, long userId);

    List<Long> getLikesList(long filmId);

    Map<Long, List<Like>> getLikesByIds(List<Long> filmIds);

    List<Long> getTheBestFilms(int count);
}
