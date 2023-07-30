package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class FriendDao implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public boolean addAsFriend(long userId, long friendId) {
        Friend friend = Friend.builder()
                .userId(userId)
                .friendId(friendId)
                .build();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("friends");
        return false;
    }

    @Override
    public boolean removeFromFriends(long userId, long friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sqlQuery, userId, friendId) > 0;
    }

    @Override
    public List<Long> getListOfFriends(long userId) {
        String sqlQuery = "SELECT friend_id FROM friends WHERE user_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
    }

    @Override
    public List<Long> getAListOfMutualFriends(long userId, long otherId) {
        String sqlQuery = "SELECT friend_id " +
                "FROM (SELECT * FROM friends WHERE user_id = ? OR user_id = ?) " +
                "GROUP BY friend_id HAVING (COUNT(*) > 1";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, userId, otherId);
    }
}
