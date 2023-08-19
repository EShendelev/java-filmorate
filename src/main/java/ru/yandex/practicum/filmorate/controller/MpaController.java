package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {
    private final MpaService mpaService;

    private static final String URI = "/mpa";
    private static final String NO_BODY = "no body";

    @GetMapping
    public Collection<Mpa> getMpaRating() {
        log.info("Вывод списка уровней рейтинга");
        return mpaService.getMpaRating();
    }

    @GetMapping("/{id}")
    public Mpa getMpaRatingById(@PathVariable int id) {
        log.info("Вывод уровня рейтинга id {}", id);
        return mpaService.getMpaRatingById(id);
    }


}
