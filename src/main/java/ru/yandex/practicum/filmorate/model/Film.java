package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;


@Data
public class Film {
    private final int id;
    private String name;
    private String description;
    private final LocalDateTime releaseDate;
    Duration duration;
}
