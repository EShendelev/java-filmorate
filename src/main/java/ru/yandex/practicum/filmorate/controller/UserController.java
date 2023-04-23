package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
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
    public User createUser(@RequestBody User user) {
        User crUser = userService.createUser(user);
        log.info("create user " + crUser.getId());
        return crUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        User upUser = userService.updateUser(user);
        log.info("User id " + upUser.getId() + " update");
        return upUser;
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") int id) {
        User user = null;
        try {
            user = userService.findUserById(id);
            log.info("User id " + user.getId() + " found");
        } catch (UserNotExistException e) {
            log.info(e.getMessage());
        }

        return user;
    }


}
