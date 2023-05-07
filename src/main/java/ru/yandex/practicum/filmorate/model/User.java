package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Email должен иметь формат email@domain.net")
    private String email;
    @NotBlank(message = "Login не может быть пустым")
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends; // Набор с ID друзей
    private Set<Long> likedFilms = new HashSet<>(); //Набор ID фильмов, которые лайкнул пользователь

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public User(Long id, String email, String login, String name, LocalDate birthday, Set<Long> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = friends;
    }
}
