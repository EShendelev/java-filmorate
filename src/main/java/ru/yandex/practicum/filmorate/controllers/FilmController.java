package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

@RestController
public class FilmController {
    private Map<Integer, Film> films;
}
