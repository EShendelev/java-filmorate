package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {
    Collection<User> findAll();

    User add(User user);

    User update(User user);

    List<User> getUsersByListIds(List<Long> ids);

    List<User> getFriendsByUserId(long userId);

    boolean checkById(long id);

    User findById(Long id);
}
