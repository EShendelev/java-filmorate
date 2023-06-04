package ru.yandex.practicum.filmorate.storage.dao;

import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.util.List;

public class LikeDao implements LikeStorage {

    @Override
    public boolean addLike(long filmId, long userId) {
        return false;
    }

    @Override
    public boolean unlike(long filmId, long userId) {
        return false;
    }

    @Override
    public List<Long> getListOfLikes(long filmId) {
        return null;
    }

    @Override
    public List<Long> getTheBestFilms(int count) {
        return null;
    }
}
