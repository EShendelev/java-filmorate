package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Класс, представляющий модель отзыва.
 */
@Data
@Builder
@AllArgsConstructor
public class Review {
    /**
     * Идентификатор отзыва.
     */
    protected int reviewId;

    /**
     * Текстовое содержание отзыва.
     */
    @NotBlank
    protected String content;

    /**
     * Тип отзыва — негативный/положительный.
     */
    @JsonProperty("isPositive")
    @NotNull
    protected Boolean isPositive;

    /**
     * Идентификатор пользователя, оставившего отзыв.
     */
    @NotNull
    protected long userId;

    /**
     * Идентификатор фильма, к которому оставлен отзыв.
     */
    protected long filmId;

    /**
     * Рейтинг полезности.
     */
    protected int useful;
}
