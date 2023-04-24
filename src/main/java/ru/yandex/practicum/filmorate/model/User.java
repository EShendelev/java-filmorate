package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private int id;
    @NotBlank(message = "Email не должен быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Email должен иметь формат email@domain.net")
    private String email;
    @NotBlank(message = "Login не может быть пустым")
    private String login;
    private String name;
    private LocalDate birthday;
}
