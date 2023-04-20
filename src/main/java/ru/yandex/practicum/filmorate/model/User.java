package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDateTime birthday;
}
