package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.List;

public interface FriendStorage {
    boolean addFriend(long userId, long friendId);
    boolean removeFromFriends(long userId, long friendId);
    List<Long> getListOfFriends(long userId);
    List<Long> getAListOfMutualFriends(long userId, long otherId);
}
