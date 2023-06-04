package ru.yandex.practicum.filmorate.storage.interfaces;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface FriendStorage {
    boolean addAsFriend(long userId, long friendId);
    boolean removeFromFriends(long userId, long friendId);
    List<Long> getListOfFriends(long userId);
    List<Long> getAListOfMutualFriends(long userId, long otherId);
}
