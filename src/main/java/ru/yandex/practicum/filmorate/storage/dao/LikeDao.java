package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class LikeDao implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addLike(long filmId, long userId) {
        if (checkLike(userId, filmId)) {
            return true;
        }
        String sql = "MERGE INTO likes (FILM_ID, USER_ID) KEY(FILM_ID, USER_ID) VALUES (?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, filmId, userId);
        return rowsAffected > 0;
    }

    @Override
    public boolean unlike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        return jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
    }

    @Override
    public List<Long> getLikesList(long filmId) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, filmId);
    }

    @Override
    public List<Long> getTheBestFilms(int count) {
        String sqlQuery = "SELECT films.id " +
                "FROM films " +
                "LEFT OUTER JOIN likes ON films.id = likes.film_id " +
                "GROUP BY films.id " +
                "ORDER BY COUNT(DISTINCT likes.user_id) DESC " +
                "LIMIT + ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, count);
    }

    private Map<String, Object> toMap(Like like) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_Id", like.getUserId());
        values.put("film_Id", like.getFilmId());
        return values;
    }

    private boolean checkLike(long userId, long filmId) {
        String sqlQuery = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, userId, filmId) > 0;
    }
}
