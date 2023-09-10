package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.CommonDatabaseException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaRatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Repository
@RequiredArgsConstructor
public class MpaRatingDao implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Collection<Mpa> getMpa() {
        String sqlQuery = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpaRating);
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        Mpa mpa = null;
        String sqlQuery = "SELECT * FROM mpa_rating WHERE id = ?";
        try {
            mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpaRating, mpaId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ObjectNotFoundException(String.format("МРА с ID %d не найдено", mpaId));
        } catch (DataAccessException e) {
            throw new CommonDatabaseException("Неожиданная ошибка работы с БД", e);
        }
        return mpa;
    }

    @Override
    public Map<Integer, Mpa> getMpaRatingByMpaIds(List<Integer> mpaIds) {
        String inSql = String.join(",", Collections.nCopies(mpaIds.size(), "?"));
        String sqlQuery = String.format("SELECT * FROM mpa_rating WHERE id IN (%s)", inSql);
        List<Mpa> mpasList = jdbcTemplate.query(sqlQuery, this::mapRowToMpaRating, mpaIds.toArray());
        return mpasList.stream().collect(Collectors.toMap(Mpa::getId, Function.identity()));
    }

    private Mpa mapRowToMpaRating(ResultSet resultSet, int i) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
