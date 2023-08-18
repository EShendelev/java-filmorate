package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaService mpaService;
    private final LikeStorage likeStorage;
    private final GenreService genreService;
    private final FilmGenreStorage filmGenreStorage;


    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        long filmId = simpleJdbcInsert.executeAndReturnKey(toMap(film)).longValue();

        if (!film.getGenres().isEmpty()) {
            filmGenreStorage.addGenres(film.getGenres(), filmId);
        }
        return findById(filmId);
    }

    @Override
    public Film update(Film film) {
        long id = film.getId();
        String sqlQuery = "UPDATE films SET name = ?," +
                "description = ?," +
                "release_date = ?," +
                "duration = ?," +
                "rate = ?," +
                "mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                id);

        Film filmBefore = findById(id);

        Collection<Genre> genreListBefore = filmBefore.getGenres();
        if (genreListBefore.equals(film.getGenres())) {
            return findById(id);
        }

        if (!genreListBefore.isEmpty()) {
            filmGenreStorage.deleteGenres(id);
        }

        if (!film.getGenres().isEmpty()) {
            filmGenreStorage.addGenres(film.getGenres(), id);
        }

        return findById(id);
    }


    @Override
    public Film findById(Long id) {
        Film film;
        String sqlQuery = "SELECT * FROM films WHERE id = ?";
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("Фильм с id %d не найден", id));
        }
        return film;
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getInt("rate"))
                .mpa(mpaService.getMpaRatingById(resultSet.getInt("mpa_id")))
                .likes(likeStorage.getLikesList(resultSet.getLong("id")))
                .genres(genreService.getListOfGenres(resultSet.getLong("id")))
                .build();
    }

    private Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rate", film.getRate());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }

    @Override
    public boolean checkById(long id) {
        if (id < 0) {
            throw new ObjectNotFoundException("Ошибка: id не может быть меньше или равно нулю.");
        }
        String sqlQuery = "SELECT COUNT(*) FROM films WHERE id = ?";
        boolean exists = false;
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("Фильм с id %s не найден", id));
        }
        exists = count > 0;
        return exists;
    }


}
