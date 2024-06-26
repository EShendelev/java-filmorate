package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    private int id;
    private String name;
    @JsonIgnore
    private Long filmId;
}
