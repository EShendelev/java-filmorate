package ru.yandex.practicum.filmorate.storage.interfaces;

import java.util.Collection;

public interface FriendStatusStorage {
    Collection<FriendStatus> getFriendStatus();
    FriendStatus getStatusById(String statusCode);
}
