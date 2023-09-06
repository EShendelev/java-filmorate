package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.dto.ReviewAddUpdateDto;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;

/**
 * Хранилище для отзывов.
 */
public interface ReviewStorage {
    /**
     * Добавить отзыв.
     *
     * @param review Отзыв для добавления
     * @return Добавленный отзыв
     */
    Review addReview(ReviewAddUpdateDto review);

    /**
     * Обновить отзыв.
     *
     * @param review Отзыв для обновления
     * @return Обновлённый отзыв
     */
    Review updateReview(ReviewAddUpdateDto review);

    /**
     * Удалить отзыв.
     *
     * @param reviewId Идентификатор отзыва
     */
    void deleteReview(int reviewId);

    /**
     * Получить отзыв по идентификатору.
     *
     * @param reviewId Идентификатор отзыва
     * @return Полученный отзыв
     */
    Review getReviewById(int reviewId);

    /**
     * Получить все отзывы, сортированные по рейтингу полезности.
     *
     * @return Список отзывов
     */
    Collection<Review> getAllReviews();

    /**
     * Поставить лайк/дизлайк отзыву.
     *
     * @param userId   Идентификатор пользователя
     * @param reviewId Идентификатор отзыва
     * @param isLike   Лайк/дизлайк
     */
    void addMarkToReview(long userId, int reviewId, boolean isLike);

    /**
     * Убрать оценку отзыву.
     *
     * @param userId   Идентификатор пользователя
     * @param reviewId Идентификатор отзыва
     */
    void removeMarkFromReview(long userId, int reviewId);

    boolean checkById(int reviewId);

    /**
     * Получить все отзывы к фильму по его идентификатору, сортированные по рейтингу полезности.
     *
     * @param filmId Идентификатор фильма
     * @param count  Количество отзывов
     * @return Список отзывов
     */
    Collection<Review> getReviewsByFilmId(long filmId, int count);
}
