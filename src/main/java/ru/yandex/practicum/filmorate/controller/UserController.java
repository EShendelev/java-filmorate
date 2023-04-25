package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserValidateFailException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("find all users");
        return userService.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        if (validateUser(user)) {
            User crUser = userService.createUser(user);
            log.info("create user: id {}", crUser.getId());
            return crUser;
        }
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (validateUser(user)) {
            User upUser = userService.updateUser(user);
            log.info("User id " + upUser.getId() + " update");
            return upUser;
        }
        return user;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") int id) {
        User user = userService.findUserById(id);
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
