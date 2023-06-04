package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.List;

public interface LikeStorage {
    boolean addLike(long filmId, long userId);
    boolean unlike(long filmId, long userId);
    List<Long> getListOfUserLikes(long filmId);
    List<Long> getTheBestFilms(int count);
}
