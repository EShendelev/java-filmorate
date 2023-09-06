package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
import java.util.*;

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
    public Map<Long, List<Director>> getDirectorsByFilmIds(List<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String sqlQuery = String.format("SELECT * FROM directors AS d " +
                "INNER JOIN film_directors AS f ON d.id = f.director_id " +
                "WHERE f.film_id IN (%s)", inSql);
        List<Director> directorsList = jdbcTemplate.query(sqlQuery, this::mapRowToDirectorWithFilmId, filmIds.toArray());
        Map<Long, List<Director>> directorsMap = new HashMap<>();
        for (Long filmId : filmIds) {
            directorsMap.put(filmId, new ArrayList<>());
        }
        for (Director director : directorsList) {
            directorsMap.get(director.getFilmId()).add(director);
        }
        return directorsMap;
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
        for (Director director : directors) {
            if (!checkById(director.getId())) {
                throw new ObjectNotFoundException("нет режиссера с таким id");
            }
        }
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Director director = directors.get(i);
                ps.setLong(1, filmId);
                ps.setInt(2, director.getId());
            }

            @Override
            public int getBatchSize() {
                return directors.size();
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
        } catch (IncorrectResultSizeDataAccessException e) {
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

    private Director mapRowToDirectorWithFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        return Director.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .filmId(resultSet.getLong("film_id"))
                .build();
    }

    private Map<String, Object> toMap(Director director) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());
        return values;
    }
}
