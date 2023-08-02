package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
public class Film {
    @PositiveOrZero (message = "ID не может быть негативным или 0")
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    private int rate;
    @NotNull(message = "MPA не может быть пустым")
    private MpaRating mpaRating;
    private List<Long> likes;
    private List<Genre> genres;

    public Integer getLikesCount() {
        return likes.size();
    }
}
