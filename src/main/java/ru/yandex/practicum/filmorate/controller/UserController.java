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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;


    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("find all users");
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        User user = userStorage.findById(id);
        log.info("Пользователь id {} найден", id);
        return user;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable("id") Long id) {
        User user = userStorage.findById(id);
        Collection<User> friendList = user.getFriends().stream()
                .map(userStorage::findById)
                .collect(Collectors.toCollection(ArrayList::new));
        log.info("Получен список друзей для пользователя id {}", id);

        return friendList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriensList(@PathVariable Long id,
                                                @PathVariable Long otherId) {
        Collection<User> commonList = userService.getCommonFriends(id, otherId);
        
        return commonList;
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        if (validateUser(user)) {
            User crUser = userStorage.add(user);
            log.info("создан пользователь: id {}", crUser.getId());
            return crUser;
        }
        return user;
    }


    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        if (validateUser(user)) {
            User upUser = userStorage.update(user);
            log.info("Пользователь id " + upUser.getId() + " обновлён");
            return upUser;
        }
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователь id {} добавлен в друзья пользователю id {}", friendId, id);
        return userStorage.findById(friendId); // возможно, это не нужно
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        return userStorage.findById(friendId);
    }

    protected boolean validateUser(User user) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new UserValidateFailException("Дата рождения не может быть в будущем");
        }
        return true;
    }
}
