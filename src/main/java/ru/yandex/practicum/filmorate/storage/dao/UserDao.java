package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class UserDao implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FriendStorage friendStorage;

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        long userId = simpleJdbcInsert.executeAndReturnKey(toMap(user)).longValue();
        return findById(userId);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?,login = ? , name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return findById(user.getId());
    }

    @Override
    public User delete(Long id) {
        return null;
    }

    @Override
    public User findById(Long id) {
        User user;
        String sqlQuery = "SELECT * FROM users WHERE id = ?";
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %s не найден", id));
        }
        return user;
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }

    private  User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(friendStorage.getListOfFriends(resultSet.getLong("id")))
                .build();
    }
}
