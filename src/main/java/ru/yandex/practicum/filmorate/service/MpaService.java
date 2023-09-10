package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaRatingStorage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRatingStorage mpaRatingStorage;

    public Collection<Mpa> getMpaRating() {
        return mpaRatingStorage.getMpa();
    }

    public Mpa getMpaRatingById(int id) {
        return mpaRatingStorage.getMpaById(id);
    }

    public Map<Integer, Mpa> getMpaRatingByMpaIds(List<Integer> mpaIds) {
        return mpaRatingStorage.getMpaRatingByMpaIds(mpaIds);
    }
}
