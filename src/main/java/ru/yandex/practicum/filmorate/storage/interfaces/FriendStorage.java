package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.List;
import java.util.Set;

public interface FriendStorage {
    boolean addAsFriend(long userId, long friendId);
    boolean removeFromFriends(long userId, long friendId);
    List<Long> getListOfFriends(long userId);
    List<Long> getAListOfMutualFriends(long userId, long otherId);
}
