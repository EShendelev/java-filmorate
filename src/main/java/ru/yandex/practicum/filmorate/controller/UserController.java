package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.log.Logger;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final String URI = "/users";
    private final String NO_BODY = "no body";

    @GetMapping
    public Collection<User> findAll() {
        Logger.logRequest(HttpMethod.GET, URI, NO_BODY);
        Logger.logInfo("Получен список всех пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        Logger.logRequest(HttpMethod.GET, URI + "/" + id, NO_BODY);
        User user = userService.findById(id);
        Logger.logInfo(String.format("Пользователь id %d найден", id));
        return user;
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriendsList(@PathVariable("id") Long id) {
        userService.findById(id);
        Logger.logRequest(HttpMethod.GET, URI + "/" + id + "/friends", NO_BODY);
        List<User> friendList = userService.getListOfFriends(id);
        Logger.logInfo(String.format("Получен список друзей для пользователя id %d", id));

        return friendList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsList(@PathVariable Long id,
                                                 @PathVariable Long otherId) {
        Logger.logRequest(HttpMethod.GET, URI + "/" + id + "/friends/common/" + otherId, NO_BODY);
        Logger.logInfo(String.format("Получен список общих друзей у пользователей с id %d и %d", id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        Logger.logRequest(HttpMethod.POST, URI, user.toString());
        User crUser = userService.add(user);
        Logger.logInfo(String.format("создан пользователь: id %d", crUser.getId()));
        return crUser;
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        Logger.logRequest(HttpMethod.PUT, URI, user.toString());
        User upUser = userService.update(user);
        Logger.logInfo("Пользователь id " + upUser.getId() + " обновлён");
        return upUser;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        Logger.logRequest(HttpMethod.PUT, URI + "/" + id + "/friends/" + friendId, NO_BODY);
        userService.addFriend(id, friendId);
        Logger.logInfo(String.format("Пользователь id %d добавлен в друзья пользователю id %d", friendId, id));
        return userService.findById(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        Logger.logRequest(HttpMethod.DELETE, URI + "/" + id + "/friends/" + friendId, NO_BODY);
        userService.deleteFriend(id, friendId);
        Logger.logInfo(String.format("Пользователь id %d удален из списка друзей пользователя id %d", friendId, id));
        return userService.findById(friendId);
    }

}
