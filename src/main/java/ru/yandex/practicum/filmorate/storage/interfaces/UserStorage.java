package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAll();

    User add(User user);

    User update(User user);

    User delete(Long id);

    User findById(Long id);

    Collection<User> getFriendList(Collection<Long> idSet);
}
