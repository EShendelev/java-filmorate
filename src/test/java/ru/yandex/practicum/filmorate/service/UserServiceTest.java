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
    void createUserWithEmptyLoginTest() {
        User user = new User(1, "email@emal.ru", "", "name", LocalDate.now());
        User user1 = new User(1, "email1@emal.ru", null, "name1", LocalDate.now().plusDays(1));
        assertThrows(UserValidateFailException.class, () -> {
            userService.createUser(user);
            userService.createUser(user1);
        });
    }

    @Test
    void createUserWithEmptyNameTest() {
        User user = new User(1, "email@emal.ru", "Login", null, LocalDate.now());
        userService.createUser(user);
        assertEquals("Login", user.getName());
    }

    @Test
    void createUserWithWrongLoginTest() {
        User user = new User(1, "email@emal.ru", "Log in", "Name", LocalDate.now());
        User user1 = new User(1, "email1@emal.ru", "", "Name1", LocalDate.now().minusDays(1));
        User user2 = new User(1, "email2@emal.ru", null, "Name2", LocalDate.now().minusDays(2));
        assertThrows(UserValidateFailException.class, () -> {
            userService.createUser(user);
            userService.createUser(user1);
            userService.createUser(user2);
        });
    }

    @Test
    void createUserWithWrongBirthdayTest() {
        User user = new User(1, "email@emal.ru", "Login", "Name", LocalDate.now().plusDays(2));
        assertThrows(UserValidateFailException.class, () -> {
            userService.createUser(user);
        });
    }

    @Test
    void createUserWithWrongEmailTest() {
        User user = new User(1, "email.emal.ru", "Login", "Name", LocalDate.now());
        User user1 = new User(1, "emalru@asdfd", "Login1", "Name1", LocalDate.now());
        User user2 = new User(1, "email.emal@ru", "Login2", "Name2", LocalDate.now());
        assertThrows(UserValidateFailException.class, () -> {
            userService.createUser(user);
            userService.createUser(user1);
            userService.createUser(user2);
        });
    }
}