package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.exception.UserValidateFailException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.utils.UserIdProvider;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    public Collection<User> findAll() {
        return users.values();
    }

    public User createUser(User user) {
        if (validateUser(user) != null) {
            throw new UserValidateFailException(validateUser(user));
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getName().isBlank()) {
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
        if (validateUser(user) != null) {
            throw new UserNotExistException(validateUser(user));
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

    private String validateUser(User user) {
        if (user.getEmail().isBlank()) {
            return "Email не может быть пустым";
        } else if (!emailValidate(user.getEmail())) {
            return "Email должен быть в формате email@domain.com";
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            return "Логин не должен быть пустым или содержать пробелы";
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            return "Дата рождения не может быть в будущем";
        }
        return null;
    }

    private boolean emailValidate(String email) {
        final String emailValidRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(emailValidRegex)
                .matcher(email)
                .matches();
    }
}
