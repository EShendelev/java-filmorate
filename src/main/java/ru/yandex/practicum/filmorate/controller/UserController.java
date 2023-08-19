package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    private static final String URI = "/users";
    private static final String NO_BODY = "no body";

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен список всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        log.info(String.format("Пользователь id %d найден", id));
        return user;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable("id") Long id) {
        userService.findById(id);
        List<User> friendList = userService.getListOfFriends(id);
        log.info(String.format("Получен список друзей для пользователя id %d", id));

        return friendList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsList(@PathVariable Long id,
                                                 @PathVariable Long otherId) {
        log.info(String.format("Получен список общих друзей у пользователей с id %d и %d", id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        User crUser = userService.add(user);
        log.info(String.format("создан пользователь: id %d", crUser.getId()));
        return crUser;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        User upUser = userService.update(user);
        log.info("Пользователь id " + upUser.getId() + " обновлён");
        return upUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info(String.format("Пользователь id %d добавлен в друзья пользователю id %d", friendId, id));
        return userService.findById(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        log.info(String.format("Пользователь id %d удален из списка друзей пользователя id %d", friendId, id));
        return userService.findById(friendId);
    }

}
