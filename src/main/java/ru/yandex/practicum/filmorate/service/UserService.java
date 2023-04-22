package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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
        int id = UserIdProvider.getIncrementId();
        user.setId(id);
        try {
            validateUser(user);
        } catch (UserValidateFailException e) {
            log.debug(e.getMessage());

        }
        users.put(id, user);
        return user;
    }

    public User updateUser(User user) {
        try {
            validateUser(user);
        } catch (UserValidateFailException e) {
            log.debug(e.getMessage());

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

    private void validateUser(User user) throws UserValidateFailException {
        if (user.getEmail().isBlank()) {
            throw new UserValidateFailException("Email не может быть пустым");
        } else if (!emailValidate(user.getEmail())) {
            throw new UserValidateFailException("Email должен быть в формате email@domain.com");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new UserValidateFailException("Логин не должен быть пустым или содержать пробелы");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidateFailException("Дата рождения не может быть в будущем");
        }
    }

    private boolean emailValidate(String email) {
        final String emailValidRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        return Pattern.compile(emailValidRegex)
                .matcher(email)
                .matches();
    }
}
