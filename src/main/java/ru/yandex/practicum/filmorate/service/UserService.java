package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.utils.UserIdProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        int id = UserIdProvider.getIncrementId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new FilmNotExistException(String.format("Пользователь с  id %d не найден", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    public User findUserById(int id) throws UserNotExistException {
        if (!users.containsKey(id)) {
            throw new UserNotExistException("Пользователь не найден");
        }
        return users.get(id);
    }
}
