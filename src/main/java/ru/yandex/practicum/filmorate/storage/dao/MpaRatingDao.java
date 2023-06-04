package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;


@Repository
@RequiredArgsConstructor
public class MpaRatingDao implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<MpaRating> getMpa() {
        String sqlQuery = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpaRating);
    }

    @Override
    public MpaRating getMpaById(int mpaId) {
        MpaRating mpaRating = null;
        String sqlQuery = "SELECT * FROM mpa_rating WHERE id = ?";
        try {
            mpaRating = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpaRating, mpaId);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("МРА с ID %d не найдено", mpaId));
        }
        return mpaRating;
    }

    private MpaRating mapRowToMpaRating(ResultSet resultSet, int i) throws SQLException {
        return MpaRating.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
