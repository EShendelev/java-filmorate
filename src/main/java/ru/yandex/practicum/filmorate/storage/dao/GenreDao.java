package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class GenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Collection<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreById(int genreId) {
        Genre genre;
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ObjectNotFoundException(String.format("Жанр с ID %d не найден", genreId));
        }
        return genre;
    }

    @Override
    public List<Genre> getFilmGenres(long filmId) {
        String sqlQuery = "SELECT * FROM genres AS g " +
                "INNER JOIN film_genre AS f ON g.id = f.genre_id " +
                "WHERE f.film_id = ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, filmId);
    }

    @Override
    public Map<Long, List<Genre>> getGenresByIds(List<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String sqlQuery = "SELECT * FROM genres AS g " +
                "INNER JOIN film_genre AS f ON g.id = f.genre_id " +
                "WHERE f.film_id IN (%s)".formatted(inSql);
        List<Genre> genresList = jdbcTemplate.query(sqlQuery, this::mapRowToGenreWithFilmId, filmIds.toArray());
        Map<Long, List<Genre>> genresMap = new HashMap<>();
        for (Long filmId : filmIds) {
            genresMap.put(filmId, new ArrayList<>());
        }
        for (Genre genre : genresList) {
            genresMap.get(genre.getFilmId()).add(genre);
        }
        return genresMap;
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }

    private Genre mapRowToGenreWithFilmId(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .filmId(resultSet.getLong("film_id"))
                .build();
    }
}
