package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.utils.UserIdProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        int id = UserIdProvider.getIncrementId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new FilmNotExistException(String.format("Пользователь с  id %d не найден", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User delete(int id) {
        return null;
    }

    @Override
    public User findById(int id) throws UserNotExistException {
        if (!users.containsKey(id)) {
            throw new UserNotExistException("Пользователь не найден");
        }
        return users.get(id);
    }
}
