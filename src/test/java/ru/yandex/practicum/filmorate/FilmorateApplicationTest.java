package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmorateApplicationTest {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final FilmStorage filmStorage;
    private final FriendStorage friendStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;
    private final MpaRatingStorage mpaRatingStorage;

    @BeforeEach
    void tearDown() {
        jdbcTemplate.update("DELETE FROM likes;");
        jdbcTemplate.update("DELETE FROM film_genre;");
        jdbcTemplate.update("DELETE FROM friends;");
        jdbcTemplate.update("DELETE FROM users;");
        jdbcTemplate.update("DELETE FROM films;");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1;");
        jdbcTemplate.update("ALTER TABLE films ALTER COLUMN id RESTART WITH 1;");
    }

    @Test
    void addUserTest() {
        User user = User.builder()
                .email("user@email.net")
                .login("user23")
                .name("User")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User newUser = userStorage.add(user);
        user.setId(1L);
        assertThat(user, equalTo(newUser));
    }

    @Test
    void updateUserTest() {
        User user = User.builder()
                .email("user@email.net")
                .login("user23")
                .name("User")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User baseUser = userStorage.add(user);
        User upUser = User.builder()
                .id(baseUser.getId())
                .email("upUser@email.net")
                .login("upUser23")
                .name("upUser")
                .birthday(LocalDate.of(1991, 11, 11))
                .friends(new ArrayList<>())
                .build();
        User updateUser = userStorage.update(upUser);
        assertThat("Пользователь не обновлен", upUser, equalTo(updateUser));
    }

    @Test
    void updateUserFailedTest() {
        User user = User.builder()
                .id(999L)
                .email("user@email.net")
                .login("user23")
                .name("User")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        ObjectNotFoundException e = Assertions.assertThrows(
                ObjectNotFoundException.class, () -> userStorage.update(user));
        assertThat("Пользователь с id 999 не найден", equalTo(e.getMessage()));
    }

    @Test
    void getUsersEmptyListTest() {
        Collection<User> users = userStorage.findAll();
        assertThat("Список пользователей не пуст", users, empty());
    }

    @Test
    void getUsersListTest() {
        User user1 = User.builder()
                .email("user1@email.net")
                .login("user1")
                .name("User1")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User user2 = User.builder()
                .email("User2@email.net")
                .login("User2")
                .name("User2")
                .birthday(LocalDate.of(1991, 11, 11))
                .friends(new ArrayList<>())
                .build();
        User addUser1 = userStorage.add(user1);
        User addUser2 = userStorage.add(user2);

        assertThat("Список пользователей пуст", userStorage.findAll(), hasSize(2));
        assertThat("User1 не найден", userStorage.findAll(), hasItem(addUser1));
        assertThat("User2 не найден", userStorage.findAll(), hasItem(addUser2));
    }

    @Test
    void getUserFailedTest() {
        ObjectNotFoundException e = Assertions.assertThrows(
                ObjectNotFoundException.class, () -> userStorage.findById(1L));
        assertThat("Пользователь с id 1 не найден", equalTo(e.getMessage()));
    }

    @Test
    void getUserByIdTest() {
        User user1 = User.builder()
                .email("user1@email.net")
                .login("user1")
                .name("User1")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User addUser = userStorage.add(user1);
        assertThat(addUser, equalTo(userStorage.findById(1L)));
    }

    @Test
    void addFriendTest() {
        User user1 = User.builder()
                .email("user1@email.net")
                .login("user1")
                .name("User1")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User user2 = User.builder()
                .email("User2@email.net")
                .login("User2")
                .name("User2")
                .birthday(LocalDate.of(1991, 11, 11))
                .friends(new ArrayList<>())
                .build();
        User addUser1 = userStorage.add(user1);
        User addUser2 = userStorage.add(user2);
        friendStorage.addFriend(1L, 2L);
        assertThat("User2 не добавлен в список друзей User1", userStorage.findById(1L).getFriends(), hasItem(2L));
        assertThat("Список друзей User2 не пуст", userStorage.findById(2L).getFriends(), empty());
    }

    @Test
    void removeFromFriendTest() {
        User user1 = User.builder()
                .email("user1@email.net")
                .login("user1")
                .name("User1")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        User user2 = User.builder()
                .email("User2@email.net")
                .login("User2")
                .name("User2")
                .birthday(LocalDate.of(1991, 11, 11))
                .friends(new ArrayList<>())
                .build();
        User addUser1 = userStorage.add(user1);
        User addUser2 = userStorage.add(user2);
        friendStorage.addFriend(1L, 2L);
        assertThat("Список друзей User1 пуст", userStorage.findById(1L).getFriends(), hasItem(2L));
        friendStorage.removeFromFriends(1L, 2L);
        assertThat("Список друзей User1 не пуст", userStorage.findById(1L).getFriends(), empty());
    }

    @Test
    void getFriendListTest() {
        User user1 = User.builder()
                .email("user1@email.net")
                .login("User1")
                .name("User1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user2 = User.builder()
                .email("user2@email.net")
                .login("User2")
                .name("User2")
                .birthday(LocalDate.of(1992, 1, 1))
                .build();
        User user3 = User.builder()
                .email("user3@email.net")
                .login("User3")
                .name("User3")
                .birthday(LocalDate.of(1993, 1, 1))
                .build();
        User addUser1 = userStorage.add(user1);
        User addUser2 = userStorage.add(user2);
        User addUser3 = userStorage.add(user3);

        friendStorage.addFriend(1L, 2L);
        friendStorage.addFriend(1L, 3L);
        assertThat("Список  друзей User1 не содержит идентификаторы пользоватей User2 и User3",
                friendStorage.getListOfFriends(1L), contains(2L, 3L));
    }

    @Test
    void getListOfMutualFriendTest() {
        User user1 = User.builder()
                .email("user1@email.net")
                .login("User1")
                .name("User1")
                .birthday(LocalDate.of(1991, 1, 1))
                .build();
        User user2 = User.builder()
                .email("user2@email.net")
                .login("User2")
                .name("User2")
                .birthday(LocalDate.of(1992, 1, 1))
                .build();
        User user3 = User.builder()
                .email("user3@email.net")
                .login("User3")
                .name("User3")
                .birthday(LocalDate.of(1993, 1, 1))
                .build();
        User addUser1 = userStorage.add(user1);
        User addUser2 = userStorage.add(user2);
        User addUser3 = userStorage.add(user3);
        friendStorage.addFriend(1L, 3L);
        friendStorage.addFriend(2L, 3L);
        assertThat("Список общих друзей User1 и User2 не содержит User3",
                friendStorage.getListOfMutualFriends(1L, 2L), contains(3L));
    }

    @Test
    void addFilmTest() {
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        Film addFilm = filmStorage.add(film);
        film.setId(1L);
        assertThat(film, equalTo(addFilm));
    }

    @Test
    void updateFilmTest() {
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        Film addFilm = filmStorage.add(film);
        Film upFilm = Film.builder()
                .id(1L)
                .name("Как приручить дракона 2")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        Film updateFilm = filmStorage.update(upFilm);
        assertThat("Фильм не обновлен", upFilm, equalTo(updateFilm));
    }

    @Test
    void updateFailFilmTest() {
        Film upFilm = Film.builder()
                .id(999L)
                .name("Как приручить дракона 2")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();

        ObjectNotFoundException e = Assertions.assertThrows(
                ObjectNotFoundException.class, () -> filmStorage.update(upFilm));
        assertThat("Фильм с id 999 не найден", equalTo(e.getMessage()));
    }

    @Test
    void getEmptyFilmListTest() {
        Collection<Film> films = filmStorage.findAll();
        assertThat("Список фильмов не пуст", films, empty());
    }

    @Test
    void getFilmsListTest() {
        Film film1 = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        Film film2 = Film.builder()
                .name("Как приручить дракона 2")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        Film addFilm1 = filmStorage.add(film1);
        Film addFilm2 = filmStorage.add(film2);

        assertThat("Список фильмов пуст", filmStorage.findAll(), hasSize(2));
        assertThat("Film1 не найден", filmStorage.findAll(), hasItem(addFilm1));
        assertThat("Film2 не найден", filmStorage.findAll(), hasItem(addFilm2));
    }

    @Test
    void getFilmByIdTest() {
        Film film1 = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        Film addFilm1 = filmStorage.add(film1);
        assertThat(addFilm1, equalTo(filmStorage.findById(1L)));
    }

    @Test
    void getEmptyLikeListTest() {
        Collection<Long> likes = likeStorage.getLikesList(1L);
        assertThat("Список лайков не пуст", likes, hasSize(0));
    }

    @Test
    void addLikeTest() {
        User user = User.builder()
                .email("user@email.net")
                .login("user")
                .name("User")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        User addUser = userStorage.add(user);
        Film addFilm = filmStorage.add(film);
        likeStorage.addLike(1L, 1L);
        assertThat(String.format("%s не поставил лайк фильму \"%s\"", addUser.getName(), addFilm.getName()),
                filmStorage.findById(1L).getLikes(), hasItem(1L));
    }

    @Test
    void unlikeTest() {
        User user = User.builder()
                .email("user@email.net")
                .login("user")
                .name("User")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        User addUser = userStorage.add(user);
        Film addFilm = filmStorage.add(film);
        likeStorage.addLike(1L, 1L);
        likeStorage.unlike(1L, 1L);
        assertThat("Список лайков не пуст",
                filmStorage.findById(1L).getLikes(), empty());
    }

    @Test
    void getLikeListTest() {
        User user = User.builder()
                .email("user@email.net")
                .login("user")
                .name("User")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        User addUser = userStorage.add(user);
        Film addFilm = filmStorage.add(film);
        likeStorage.addLike(1L, 1L);
        assertThat("Список лайков не содержит пользователя с id 1L", likeStorage.getLikesList(1L),
                contains(1L));
    }

    @Test
    void getPopularFilmsTest() {
        User user1 = User.builder()
                .email("user@email.net")
                .login("user")
                .name("User")
                .birthday(LocalDate.of(1991, 1, 1))
                .friends(new ArrayList<>())
                .build();
        Film film1 = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        User user2 = User.builder()
                .email("user2@email.net")
                .login("user2")
                .name("User2")
                .birthday(LocalDate.of(1990, 1, 1))
                .friends(new ArrayList<>())
                .build();
        Film film2 = Film.builder()
                .name("Как приручить дракона 2")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        User addUser1 = userStorage.add(user1);
        Film addFilm1 = filmStorage.add(film1);
        User addUser2 = userStorage.add(user2);
        Film addFilm2 = filmStorage.add(film2);
        likeStorage.addLike(1L, 1L);
        likeStorage.addLike(1L, 2L);
        assertThat("Список лучших фильмов отличается от [1, 2]", likeStorage.getTheBestFilms(5),
                contains(1L, 2L));
        assertThat("Список лучших фильмов отличается от [1]", likeStorage.getTheBestFilms(1),
                contains(1L));
    }

    @Test
    void getGenreByIdTest() {
        Genre genre1 = Genre.builder()
                .id(1)
                .name("Комедия")
                .build();
        Genre genre6 = Genre.builder()
                .id(6)
                .name("Боевик")
                .build();
        assertThat(genreStorage.getGenreById(1), equalTo(genre1));
        assertThat(genreStorage.getGenreById(6), equalTo(genre6));
    }

    @Test
    void getGenresListTest() {
        Genre genre = Genre.builder()
                .id(6)
                .name("Боевик")
                .build();
        assertThat(genreStorage.getGenres(), hasSize(6));
        assertThat(genreStorage.getGenres(), hasItem(genre));
    }

    @Test
    void addGenreToFilmTest() {
        Genre genre3 = Genre.builder()
                .id(3)
                .name("Мультфильм")
                .build();
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(new ArrayList<>())
                .build();
        Film addFilm = filmStorage.add(film);
        filmGenreStorage.addGenres(List.of(genre3), 1L);
        assertThat(filmStorage.findById(1L).getGenres(), hasItem(genre3));
    }

    @Test
    void getListOfGenresTest() {
        Genre genre3 = Genre.builder()
                .id(3)
                .name("Мультфильм")
                .build();
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(List.of(genre3))
                .build();
        Film addFilm1 = filmStorage.add(film);
        assertThat(filmGenreStorage.getListOfGenres(1L), hasItem(genre3.getId()));
    }

    @Test
    void deleteGenreTest() {
        Genre genre3 = Genre.builder()
                .id(3)
                .name("Мультфильм")
                .build();
        Film film = Film.builder()
                .name("Как приручить дракона")
                .description("Сын вождя заводит дружбу с драконом — врагом его племени. " +
                        "История о том, что ум и сочувствие куда важнее силы")
                .releaseDate(LocalDate.of(2010, 3, 18))
                .duration(98)
                .rate(1)
                .mpa(Mpa.builder().id(1).name("G").build())
                .likes(new ArrayList<>())
                .genres(List.of(genre3))
                .build();
        Film addFilm = filmStorage.add(film);
        filmGenreStorage.deleteGenres(1L);
        assertThat(filmGenreStorage.getListOfGenres(1L), empty());
    }

    @Test
    void getMpaRatingListTest() {
        Mpa mpa1 = Mpa.builder()
                .id(1)
                .name("G")
                .build();
        assertThat(mpaRatingStorage.getMpa(), hasSize(5));
        assertThat(mpaRatingStorage.getMpa(), hasItem(mpa1));
    }

    @Test
    void getMpaRatingById() {
        Mpa mpa1 = Mpa.builder()
                .id(1)
                .name("G")
                .build();
        Mpa mpa5 = Mpa.builder()
                .id(5)
                .name("NC-17")
                .build();
        assertThat(mpaRatingStorage.getMpaById(1), equalTo(mpa1));
        assertThat(mpaRatingStorage.getMpaById(5), equalTo(mpa5));
    }
}
