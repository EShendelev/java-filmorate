package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidateFailException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("find all users");
        return userStorage.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        if (validateUser(user)) {
            User crUser = userStorage.add(user);
            log.info("create user: id {}", crUser.getId());
            return crUser;
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (validateUser(user)) {
            User upUser = userStorage.update(user);
            log.info("User id " + upUser.getId() + " update");
            return upUser;
        }
        return user;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") int id) {
        User user = userStorage.findById(id);
        log.info("User id {} found", id);
        return user;
    }

    protected boolean validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidateFailException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}
