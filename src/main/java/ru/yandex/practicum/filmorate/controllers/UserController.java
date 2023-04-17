package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

@RestController
public class UserController {
    private Map<Integer, User> users;

}
