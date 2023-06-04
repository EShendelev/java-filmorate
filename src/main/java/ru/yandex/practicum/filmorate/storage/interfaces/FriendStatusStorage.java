package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.FriendStatus;

import java.util.Collection;

public interface FriendStatusStorage {
    Collection<FriendStatus> getFriendStatus();
    FriendStatus getStatusById(int mpaId);
}
