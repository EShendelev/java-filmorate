package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен весь список пользователей");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        User user = userService.findById(id);
        log.info("Пользователь id {} найден", id);
        return user;
    }

//    @GetMapping("/{id}/friends")
//    public Collection<User> getFriendsList(@PathVariable("id") Long id) {
//        User user = userService.findById(id);
//        Collection<User> friendList = userService.getFriendsList(user.getFriends());
//        log.info("Получен список друзей для пользователя id {}", id);
//
//        return friendList;
//    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriendsList(@PathVariable Long id,
                                                 @PathVariable Long otherId) {

        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        //   log.info("создан пользователь: id {}", crUser.getId());
            return userService.add(user);
    }                                          

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        // log.info("Пользователь id " + upUser.getId() + " обновлён");
            return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable Long id,
                          @PathVariable Long friendId) {
        userService.addFriend(id, friendId);
        log.info("Пользователь id {} добавлен в друзья пользователю id {}", friendId, id);
        return userService.findById(friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long id,
                             @PathVariable Long friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Пользователь id {} удален из списка друзей пользователя id {}", friendId, id);
        return userService.findById(friendId);
    }

}
