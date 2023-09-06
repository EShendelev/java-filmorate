package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    public Map<Long, List<Like>> getLikesByIds(List<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String sqlQuery = String.format("SELECT * FROM likes WHERE film_id IN (%s)", inSql);
        List<Like> likesList = jdbcTemplate.query(sqlQuery, this::mapRowToLike, filmIds.toArray());
        Map<Long, List<Like>> likesMap = new HashMap<>();
        for (Long filmId : filmIds) {
            likesMap.put(filmId, new ArrayList<>());
        }
        for (Like like : likesList) {
            likesMap.get(like.getFilmId()).add(like);
        }
        return likesMap;
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

    private Like mapRowToLike(ResultSet resultSet, int rowNum) throws SQLException {
        return Like.builder()
                .filmId(resultSet.getLong("film_id"))
                .userId(resultSet.getLong("user_id"))
                .build();
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
