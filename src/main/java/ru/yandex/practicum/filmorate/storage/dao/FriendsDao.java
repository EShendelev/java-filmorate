package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FriendsDao implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean addFriend(long userId, long friendId) {
        Friends friends = Friends.builder()
                .userId(userId)
                .friendId(friendId)
                .build();
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("friends");
        return simpleJdbcInsert.execute(toMap(friends)) > 0;
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
    public List<Long> getListOfMutualFriends(long userId, long otherId) {
        String sqlQuery = "SELECT friend_id " +
                "FROM (SELECT * FROM friends WHERE user_id = ? OR user_id = ?) " +
                "GROUP BY friend_id HAVING (COUNT(*) > 1)";
        List<Long> result = jdbcTemplate.queryForList(sqlQuery, Long.class, userId, otherId);
        return result;
    }

    private Map<String, Object> toMap(Friends friends) {
        Map<String, Object> values = new HashMap<>();
        values.put("user_id", friends.getUserId());
        values.put("friend_id", friends.getFriendId());
        return values;
    }
}
