package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.FriendStatus;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStatusStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Repository
@RequiredArgsConstructor
public class FriendStatusDao implements FriendStatusStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<FriendStatus> getFriendStatus() {
        String sqlQuery = "SELECT * FROM status_code";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFriendStatus);
    }

    @Override
    public FriendStatus getStatusById(String statusCode) {
        FriendStatus friendStatus = null;
        String sqlQuery = "SELECT * FROM status_code WHERE code = ?";
        try {
            friendStatus = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFriendStatus, statusCode);
        } catch (DataAccessException e) {
            throw new ObjectNotFoundException(String.format("Не найдено статуса с кодом %s", statusCode));
        }
        return friendStatus;
    }

    private FriendStatus mapRowToFriendStatus(ResultSet resultSet, int rowNum) throws SQLException {
        return FriendStatus.builder()
                .code(resultSet.getString("code"))
                .name(resultSet.getString("name"))
                .build();
    }
}
