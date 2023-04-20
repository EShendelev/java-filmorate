package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    public Collection<User> findAll() {
        User user = new User(1, "email@email.ru", "login", "Name Surname", LocalDateTime.now());
        users.put(user.getEmail(), user);
        return users.values();
    }

    public User createUser(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public User updateUser(User user) {
        users.put(user.getEmail(), user);
        return user;
    }

    public User findUserByEmail(String email) {
        return users.get(email);
    }
}
