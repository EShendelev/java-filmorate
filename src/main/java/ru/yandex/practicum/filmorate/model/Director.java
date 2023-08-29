package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
public class Director {
    @Positive(message = "ID не может быть меньше или равно нулю")
    private int id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
}
