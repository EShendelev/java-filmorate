package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class Film {
    @Positive(message = "ID не может быть меньше или равно нулю")
    private Long id;
    @NotBlank(message = "Имя не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания - 200 символов")
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;
    private int rate;
    @NotNull
    private Mpa mpa;
    private List<Long> likes;
    private List<Genre> genres;
    private List<Director> directors;

    public Integer getLikesCount() {
        return likes.size();
    }

    public List<Genre> getGenres() {
        if (this.genres == null) {
            return new ArrayList<>();
        }
        return this.genres;
    }

    public List<Director> getDirectors() {
        if (this.directors == null) {
            return new ArrayList<>();
        }
        return this.directors;
    }
}
