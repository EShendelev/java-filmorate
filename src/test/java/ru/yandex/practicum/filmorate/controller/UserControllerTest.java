package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.exception.UserValidateFailException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserService userService;
    UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController(userService);
    }

    @Test
    void validateUserWithWrongBirthdayTest() {
        User user = new User(1, "email@emal.ru", "Login", "Name", LocalDate.now().plusDays(2));
        assertThrows(UserValidateFailException.class, () -> {
            userController.validateUser(user);
        });
    }
}