package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DirectorDao implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getDirectors() {
        String sqlQuery = "SELECT * FROM directors";
        return jdbcTemplate.query(sqlQuery, this::mapRowToDirector);
    }

    @Override
    public List<Director> getDirectorsByFilmId(long filmId) {
        String sqlQuery = "SELECT * FROM directors AS d " +
                "INNER JOIN film_directors AS f ON d.id = f.director_id " +
                "WHERE f.film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToDirector, filmId);
    }

    @Override
    public Director addDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");
        int directorId = simpleJdbcInsert.executeAndReturnKey(toMap(director)).intValue();
        return getDirectorById(directorId);
    }

    @Override
    public void addDirectors(List<Director> directors, long filmId) {
        String sqlQuery = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
        List<Director> uniqueDirectors = directors.stream()
                .distinct()
                .collect(Collectors.toList());
        for (Director director : directors) {
            if (!checkById(director.getId())) {
                throw new ObjectNotFoundException("нет режиссера с таким id");
            }
        }
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Director director = uniqueDirectors.get(i);
                ps.setLong(1, filmId);
                ps.setInt(2, director.getId());
            }

            @Override
            public int getBatchSize() {
                return uniqueDirectors.size();
            }
        });
    }

    @Override
    public Director updateDirector(Director director) {
        if (!checkById(director.getId())) {
            throw new ObjectNotFoundException("нет режиссера с таким id");
        }
        String sqlQuery = "UPDATE directors SET name = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        return director;
    }

    @Override
    public Director getDirectorById(int id) {
        if (!checkById(id)) {
            throw new ObjectNotFoundException("нет режиссера с таким id");
        }
        Director director;
        String sqlQuery = "SELECT * FROM directors WHERE id = ?";
        try {
            director = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToDirector, id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("Режиссер с ID %d не найден", id));
        }
        return director;
    }

    @Override
    public void deleteDirector(int directorId) {
        if (!checkById(directorId)) {
            throw new ObjectNotFoundException("нет режиссера с таким id");
        }
        String sqlQuery = "DELETE FROM directors WHERE id = ?";
        jdbcTemplate.update(sqlQuery, directorId);
    }

    @Override
    public void deleteDirectorsFromFilm(long filmId) {
        String sqlQuery = "DELETE FROM film_directors WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }


    @Override
    public boolean checkById(int id) {
        if (id < 0) {
            throw new ObjectNotFoundException("Ошибка: id не может быть меньше или равно нулю.");
        }
        String sqlQuery = "SELECT COUNT(*) FROM directors WHERE id = ?";
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count > 0;
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Map<String, Object> toMap(Director director) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());
        return values;
    }
}
