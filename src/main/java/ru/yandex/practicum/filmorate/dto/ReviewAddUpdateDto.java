package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ReviewAddUpdateDto {
    protected int reviewId;

    @NotBlank
    protected String content;

    @JsonProperty("isPositive")
    @NotNull
    protected Boolean isPositive;

    @NotNull
    protected long userId;

    protected long filmId;

    protected int useful = 0;
}
