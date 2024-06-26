package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.CommonDatabaseException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

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
        return setAnotherFieldsForFilms(jdbcTemplate.query(sqlQuery, this::mapRowToFilm));
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
            if (!genreListUpdateFilm.isEmpty()) {
                filmGenreStorage.addGenres(film.getGenres(), id);
            }
        }
        if (!new HashSet<>(filmBefore.getDirectors()).containsAll(film.getDirectors()) ||
                !new HashSet<>(film.getDirectors()).containsAll(filmBefore.getDirectors())) {
            if (!filmBefore.getDirectors().isEmpty()) {
                directorStorage.deleteDirectorsFromFilm(id);
            }
            if (!film.getDirectors().isEmpty()) {
                directorStorage.addDirectors(film.getDirectors(), id);
            }
        }
        return findById(id);
    }

    @Override
    public List<Film> getFilmsByDirectorSorted(int directorId, SortBy sortBy) {
        if (!directorStorage.checkById(directorId)) {
            throw new ObjectNotFoundException("нет режиссера с таким id");
        }
        String sqlQuery = "";
        if (sortBy.equals(SortBy.YEAR)) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "WHERE fd.director_id = ?\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY f.release_date";
        } else if (sortBy.equals(SortBy.LIKES)) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "\tcount(SELECT * FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "WHERE fd.director_id = ?\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes";
        } else if (sortBy.equals(SortBy.NO_SORT)) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "WHERE fd.director_id = ?" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n";
        }
        return setAnotherFieldsForFilms(jdbcTemplate.query(sqlQuery, this::mapRowToFilm, directorId));
    }

    @Override
    public List<Film> getLikedFilms(long id) {
        String sql = "SELECT * FROM films f JOIN likes l ON f.id = l.film_id AND l.user_id = ?";
        return setAnotherFieldsForFilms(jdbcTemplate.query(sql, this::mapRowToFilm, id));
    }

    @Override
    public List<Film> searchByFilmAndDirectorSorted(String query, SearchBy searchBy) {
        String sqlQuery = "";
        if (searchBy == SearchBy.TITLE) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "(SELECT count(l.film_id) FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "WHERE f.name ILIKE CONCAT('%',?,'%')\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes\n";
        } else if (searchBy == SearchBy.DIRECTOR) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "(SELECT count(l.film_id) FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "INNER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "INNER JOIN directors AS d ON d.id = fd.director_id\n" +
                    "WHERE d.name ILIKE CONCAT('%',?,'%')\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes";
        } else if (searchBy == SearchBy.TITLEANDDIRECTOR) {
            sqlQuery = "SELECT f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id,\n" +
                    "(SELECT count(l.film_id) FROM likes AS l WHERE l.film_id = f.id) AS film_likes\n" +
                    "FROM films AS f\n" +
                    "LEFT OUTER JOIN film_directors AS fd ON f.id = fd.film_id\n" +
                    "LEFT OUTER JOIN directors AS d ON d.id = fd.director_id\n" +
                    "WHERE f.name ILIKE CONCAT('%',?,'%') OR d.name ILIKE CONCAT('%',?,'%')\n" +
                    "GROUP BY f.id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id\n" +
                    "ORDER BY film_likes DESC;";
            return setAnotherFieldsForFilms(jdbcTemplate.query(sqlQuery, this::mapRowToFilm, query, query));
        }
        return setAnotherFieldsForFilms(jdbcTemplate.query(sqlQuery, this::mapRowToFilm, query));
    }

    @Override
    public Collection<Film> getPopularFilm(Integer count, Integer genreId, Integer year) {
        String sql = "SELECT f.*, COUNT(DISTINCT l.user_id) AS like_count " +
                "FROM films f " +
                "LEFT JOIN likes l ON f.id = l.film_id " +
                "LEFT JOIN film_genre fg ON f.id = fg.film_id " +
                "LEFT JOIN genres g ON fg.genre_id = g.id " +
                "WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (genreId != null) {
            sql += " AND fg.genre_id = ?";
            params.add(genreId);
        }

        if (year != null) {
            sql += " AND YEAR(f.release_date) = ?";
            params.add(year);
        }

        sql += " GROUP BY f.id, f.name " +
                "ORDER BY like_count DESC LIMIT ?";
        params.add(count);
        Collection<Film> films = setAnotherFieldsForFilms(jdbcTemplate.query(sql, this::mapRowToFilm, params.toArray()));

        return films;
    }

    @Override
    public Collection<Film> getCommonFilms(Integer userId, Integer friendId) {
        String sql = "SELECT DISTINCT f.* FROM films f " +
                "JOIN likes l1 ON f.id = l1.film_id AND l1.user_id = ? " +
                "JOIN likes l2 ON f.id = l2.film_id AND l2.user_id = ?";

        Collection<Film> films = setAnotherFieldsForFilms(jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId));
        return films;
    }


    @Override
    public Film findById(Long id) {
        Film film;
        String sqlQuery = "SELECT * FROM films WHERE id = ?";
        try {
            film = setAnotherFieldsForFilm(jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id));
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new ObjectNotFoundException(String.format("Фильм с id %d не найден", id));
        } catch (DataAccessException e) {
            throw new CommonDatabaseException("Неожиданная ошибка работы с БД", e);
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
                .mpa(Mpa.builder().id(resultSet.getInt("mpa_id")).build())
                .build();
    }

    private Film setAnotherFieldsForFilm(Film film) {
        return setAnotherFieldsForFilms(List.of(film)).get(0);
    }

    private List<Film> setAnotherFieldsForFilms(List<Film> films) {
        List<Long> filmIds = films.stream().map(Film::getId).collect(Collectors.toList());
        List<Integer> mpaIds = films.stream().map(f -> f.getMpa().getId()).collect(Collectors.toList());
        Map<Integer, Mpa> mpas = mpaService.getMpaRatingByMpaIds(mpaIds);
        Map<Long, List<Like>> likes = likeStorage.getLikesByIds(filmIds);
        Map<Long, List<Genre>> genres = genreService.getGenresByIds(filmIds);
        Map<Long, List<Director>> directors = directorStorage.getDirectorsByFilmIds(filmIds);
        for (Film film : films) {
            film.setMpa(mpas.get(film.getMpa().getId()));
            film.setDirectors(directors.getOrDefault(film.getId(), new ArrayList<>()));
            film.setGenres(genres.getOrDefault(film.getId(), new ArrayList<>()));
            film.setLikes(likes.getOrDefault(film.getId(), new ArrayList<>()).stream().map(Like::getUserId).collect(Collectors.toList()));
        }
        return films;
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
