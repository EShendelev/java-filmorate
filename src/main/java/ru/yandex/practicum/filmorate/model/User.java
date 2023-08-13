package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class User {
    private Long id;
    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Email должен иметь формат email@domain.net")
    private String email;
    @NotBlank(message = "Login не может быть пустым")
    private String login;
    private String name;
    @PastOrPresent(message = "День рождения должен быть сегодня или в прошлом")
    private LocalDate birthday;
    private final List<Long> friends;
}
