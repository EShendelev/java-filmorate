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
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
    private final DirectorStorage directorStorage;

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
        directorStorage.addDirectors(film.getDirectors(), filmId);

        return findById(filmId);
    }

    @Override
    public Film update(Film film) {
        long id = film.getId();
        if (!checkById(id)) {
            throw new ObjectNotFoundException("нет фильма с таким id");
        }
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
        Collection<Genre> genreListUpdateFilm = film.getGenres();
        if (!genreListBefore.containsAll(genreListUpdateFilm) || !genreListUpdateFilm.containsAll(genreListBefore)) {
            if (!genreListBefore.isEmpty()) {
                filmGenreStorage.deleteGenres(id);
            }
            if (!film.getGenres().isEmpty()) {
                filmGenreStorage.addGenres(film.getGenres(), id);
            }
        }

        directorStorage.deleteDirectorsFromFilm(id);
        directorStorage.addDirectors(film.getDirectors(), id);

        return findById(id);
    }

    @Override
    public List<Film> getFilmsByDirectorSorted(int directorId, String sortBy) {
        if (!directorStorage.checkById(directorId)) {
            throw new ObjectNotFoundException("нет режиссера с таким id");
        }
        String sqlQuery = "";
        if (sortBy == null) {
            sortBy = "";
        }
        if (sortBy.equals("year")) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "WHERE fd.director_id = ?\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY f.release_date";
        } else if (sortBy.equals("likes")) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "\tcount(SELECT * FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "WHERE fd.director_id = ?\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes";
        } else {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "WHERE fd.director_id = ?" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n";
        }
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> getRecommendations(long id) {
        String sqlQuery = "SELECT * FROM films WHERE id IN (SELECT film_id FROM likes WHERE film_id IN" +
                " (SELECT film_id FROM likes WHERE user_id IN ((SELECT user_id FROM likes WHERE film_id IN" +
                " (SELECT film_id FROM likes WHERE user_id = ?)" +
                " AND NOT user_id = ? GROUP BY user_id ORDER BY COUNT(film_id) DESC LIMIT 1), ?)" +
                " GROUP BY film_id HAVING COUNT(user_id) = 1) AND user_id <> ?)";
        log.info(String.format("Пользователь с id=%d получил список рекомендаций", id));
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, id, id, id, id);
    }
  
    @Override
    public List<Film> searchByFilmAndDirectorSorted(String query, String by) {
        String sqlQuery = "";
        if (by.equals("title")) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "count(SELECT l.film_id FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "WHERE f.name ILIKE CONCAT('%',?,'%')\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes\n";
        } else if (by.equals("director")) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "count(SELECT l.film_id FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "INNER JOIN directors AS d ON d.id = fd.director_id\n" +
                    "WHERE d.name ILIKE CONCAT('%',?,'%')\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes";
        } else if (by.equals("director,title") || by.equals("title,director")) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "count(SELECT l.film_id FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "LEFT OUTER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "LEFT OUTER JOIN directors AS d ON d.id = fd.director_id\n" +
                    "WHERE f.name ILIKE CONCAT('%',?,'%') OR d.name ILIKE CONCAT('%',?,'%')\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes DESC";
            return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, query, query);
        }
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, query);
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
                .directors(directorStorage.getDirectorsByFilmId(resultSet.getLong("id")))
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
        int count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        exists = count > 0;
        return exists;
    }

    @Override
    public void deleteFilmById(long id) {
        if (!checkById(id)) {
            throw new ObjectNotFoundException(String.format("Фильм с id %d не найден", id));
        }
        String sqlQuery = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }
}
