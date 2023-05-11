package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.utils.UserIdProvider;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private UserStorage userStorage;
    private UserService userService;

    @BeforeEach
    void beforeEach() {
        userStorage = new InMemoryUserStorage(new UserIdProvider());
        userService = new UserService(userStorage);
    }

    @Test
    void addFriendTest() {
        User user1 = new User(1L, "email@emal.ru", "Login", "Name",
                LocalDate.now().plusDays(2), new HashSet<>());
        User user2 = new User(2L, "email1@emal.ru", "Login1", "Name",
                LocalDate.now().plusDays(4), new HashSet<>());

        userStorage.add(user1);
        userStorage.add(user2);

        userService.addFriend(1L, 2L);
        assertEquals(1, user1.getFriends().size());
        assertEquals(1, user2.getFriends().size());
        assertTrue(user1.getFriends().contains(2L));
        assertTrue(user2.getFriends().contains(1L));
    }

    @Test
    void deleteFriendTest() {
        User user1 = new User(1L, "email@emal.ru", "Login", "Name",
                LocalDate.now().plusDays(2), new HashSet<>(List.of(2L, 4L)));
        User user2 = new User(2L, "email1@emal.ru", "Login1", "Name",
                LocalDate.now().plusDays(4), new HashSet<>(List.of(1L, 3L, 4L)));
        User user3 = new User(3L, "email2@emal.ru", "Login", "Name",
                LocalDate.now().plusDays(6), new HashSet<>(List.of(2L, 4L)));
        User user4 = new User(4L, "email3@emal.ru", "Login1", "Name",
                LocalDate.now().plusDays(8), new HashSet<>(List.of(1L, 3L, 2L)));

        userStorage.add(user1);
        userStorage.add(user2);
        userStorage.add(user3);
        userStorage.add(user4);

        userService.deleteFriend(2L, 4L);
        assertEquals(2, user2.getFriends().size());
        assertEquals(2, user4.getFriends().size());
        assertArrayEquals(new Long[]{1L, 3L}, user2.getFriends().toArray());
    }

    @Test
    void getCommonFriendsTest() {
        User user1 = new User(1L, "email@emal.ru", "Login", "Name",
                LocalDate.now().plusDays(2), new HashSet<>(List.of(2L, 4L)));
        User user2 = new User(2L, "email1@emal.ru", "Login1", "Name",
                LocalDate.now().plusDays(4), new HashSet<>(List.of(1L, 3L, 4L)));
        User user3 = new User(3L, "email2@emal.ru", "Login", "Name",
                LocalDate.now().plusDays(6), new HashSet<>(List.of(2L, 4L)));
        User user4 = new User(4L, "email3@emal.ru", "Login1", "Name",
                LocalDate.now().plusDays(8), new HashSet<>(List.of(1L, 3L, 2L)));

        userStorage.add(user1);
        userStorage.add(user2);
        userStorage.add(user3);
        userStorage.add(user4);

        List<User> commonFriends = userService.getCommonFriends(2L, 4L);
        assertArrayEquals(new Long[]{1L, 3L}, commonFriends.stream().map(User::getId).toArray(Long[]::new));
    }
}