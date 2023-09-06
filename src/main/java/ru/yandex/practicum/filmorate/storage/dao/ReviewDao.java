package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dto.ReviewAddUpdateDto;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.interfaces.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class ReviewDao implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review addReview(ReviewAddUpdateDto review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reviews")
                .usingGeneratedKeyColumns("id");

        Number id = simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(review));
        return getReviewById(id.intValue());
    }

    @Override
    public Review updateReview(ReviewAddUpdateDto review) {
        if (!checkById(review.getReviewId())) {
            throw new ObjectNotFoundException("Нет отзыва с id = " + review.getReviewId());
        }
        String sqlQuery = "UPDATE REVIEWS SET CONTENT = ?," +
                "IS_POSITIVE = ?" +
                "WHERE ID = ?";
        jdbcTemplate.update(sqlQuery,
                review.getContent(),
                review.getIsPositive(),
                review.getReviewId());

        return getReviewById(review.getReviewId());
    }

    @Override
    public void deleteReview(int reviewId) {
        if (!checkById(reviewId)) {
            throw new ObjectNotFoundException("Нет отзыва с id = " + reviewId);
        }
        String query = "DELETE from REVIEWS WHERE ID = ?";
        jdbcTemplate.update(query, reviewId);
    }

    @Override
    public Review getReviewById(int reviewId) {
        if (!checkById(reviewId)) {
            throw new ObjectNotFoundException("Нет отзыва с id = " + reviewId);
        }
        String sqlQuery = "SELECT * FROM REVIEWS WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToReview, reviewId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ObjectNotFoundException(String.format("Отзыв с id %s не найден", reviewId));
        }
    }

    @Override
    public Collection<Review> getAllReviews() {
        String query = "SELECT * FROM REVIEWS ORDER BY USEFUL DESC";

        return jdbcTemplate.query(query, this::mapRowToReview);
    }

    @Transactional
    @Override
    public void addMarkToReview(long userId, int reviewId, boolean isLike) {
        if (!checkById(reviewId)) {
            throw new ObjectNotFoundException("Нет отзыва с id = " + reviewId);
        }

        String userReviewUpdateQuery = "MERGE INTO USER_REVIEW_REACTIONS (USER_ID, REVIEW_ID, IS_LIKE) " +
                "KEY(USER_ID, REVIEW_ID) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(userReviewUpdateQuery, userId, reviewId, isLike);

        String reviewUpdateQuery = "UPDATE REVIEWS SET USEFUL = USEFUL + ?" +
                "WHERE ID = ?";
        int usefulValue = isLike ? 1 : -1;
        jdbcTemplate.update(reviewUpdateQuery, usefulValue, reviewId);
    }

    @Transactional
    @Override
    public void removeMarkFromReview(long userId, int reviewId) {
        if (!checkById(reviewId)) {
            throw new ObjectNotFoundException("Нет отзыва с id = " + reviewId);
        }
        String isLikeQuery = "SELECT IS_LIKE FROM USER_REVIEW_REACTIONS WHERE USER_ID=? AND REVIEW_ID=? LIMIT 1";
        Boolean isLike = jdbcTemplate.queryForObject(isLikeQuery, Boolean.class);

        String userReviewUpdateQuery = "DELETE FROM USER_REVIEW_REACTIONS WHERE USER_ID=? AND REVIEW_ID=?";
        jdbcTemplate.update(userReviewUpdateQuery, userId, reviewId);

        String reviewUpdateQuery = "UPDATE REVIEWS SET USEFUL = USEFUL - ?" +
                "WHERE ID = ?";
        int usefulValue = Boolean.TRUE.equals(isLike) ? 1 : -1;
        jdbcTemplate.update(reviewUpdateQuery, reviewId, usefulValue);
    }

    @Override
    public boolean checkById(int reviewId) {
        if (reviewId < 0) {
            throw new ObjectNotFoundException("Ошибка: id не может быть меньше или равно нулю.");
        }
        String query = "SELECT COUNT(*) FROM REVIEWS WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(query, Integer.class, reviewId);
        return count != null && count > 0;
    }

    @Override
    public Collection<Review> getReviewsByFilmId(long filmId, int count) {
        String query = "SELECT * FROM REVIEWS" +
                " WHERE FILM_ID=?" +
                " ORDER BY USEFUL DESC" +
                " LIMIT ? ";

        return jdbcTemplate.query(query, this::mapRowToReview, filmId, count);
    }

    private Review mapRowToReview(ResultSet resultSet, int i) throws SQLException {
        return Review.builder()
                .reviewId(resultSet.getInt("id"))
                .content(resultSet.getString("content"))
                .isPositive(resultSet.getBoolean("is_positive"))
                .userId(resultSet.getLong("user_id"))
                .filmId(resultSet.getLong("film_id"))
                .useful(resultSet.getInt("useful"))
                .build();
    }
}
