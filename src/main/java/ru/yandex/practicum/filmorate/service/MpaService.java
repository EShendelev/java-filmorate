package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaRatingStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaRatingStorage mpaRatingStorage;

    public Collection<MpaRating> getMpaRating() {
        //log
        return mpaRatingStorage.getMpa();
    }

    public MpaRating getMpaRatingById(int id) {
        return mpaRatingStorage.getMpaById(id);
    }
}
