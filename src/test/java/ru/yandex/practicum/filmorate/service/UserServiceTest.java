package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.UserValidateFailException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService;

    @BeforeEach
    void beforeEach() {
        userService = new UserService();
    }

    @Test
    void validateUserWithEmptyLoginTest() {
        User user = new User(1, "email@emal.ru", "", "name", LocalDate.now());
        assertThrows(UserValidateFailException.class, () -> {
            userService.validateUser(user);
        });
    }

    @Test
    void validateUserWithWrongLoginTest() {
        User user = new User(1, "email@emal.ru", "Log in", "Name", LocalDate.now());
        assertThrows(UserValidateFailException.class, () -> {
            userService.validateUser(user);
        });
    }

    @Test
    void validateUserWithWrongLoginTest1() {
        User user = new User(1, "email1@emal.ru", "", "Name1", LocalDate.now().minusDays(1));
        assertThrows(UserValidateFailException.class, () -> {
            userService.validateUser(user);
        });
    }

    @Test
    void validateUserWithWrongLoginTest2() {
        User user = new User(1, "email2@emal.ru", null, "Name2", LocalDate.now().minusDays(2));
        assertThrows(UserValidateFailException.class, () -> {
            userService.validateUser(user);
        });
    }

    @Test
    void validateUserWithWrongBirthdayTest() {
        User user = new User(1, "email@emal.ru", "Login", "Name", LocalDate.now().plusDays(2));
        assertThrows(UserValidateFailException.class, () -> {
            userService.validateUser(user);
        });
    }
}