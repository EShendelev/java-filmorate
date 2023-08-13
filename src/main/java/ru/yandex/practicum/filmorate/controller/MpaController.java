package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    private final String URI = "/mpa";
    private final String NO_BODY = "no body";

    @GetMapping
    public Collection<Mpa> getMpaRating() {
        Logger.logRequest(HttpMethod.GET, URI, NO_BODY);
        return mpaService.getMpaRating();
    }

    @GetMapping("/{id}")
    public Mpa getMpaRatingById(@PathVariable int id) {
        Logger.logRequest(HttpMethod.GET, URI, NO_BODY);
        return mpaService.getMpaRatingById(id);
    }


}
