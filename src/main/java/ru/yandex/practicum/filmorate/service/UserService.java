package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    UserStorage userStorage;
    FilmService filmService;

    @Autowired
    public UserService(UserStorage userStorage, FilmService filmService) {
        this.userStorage = userStorage;
        this.filmService = filmService;
    }

    public void addFriend(Long userId, Long friendId) {
        Set<Long> friendsIds = userStorage.findById(userId).getFriends();
        Set<Long> friendsOfFriend = userStorage.findById(friendId).getFriends();
        friendsIds.add(friendId);
        friendsOfFriend.add(userId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        Set<Long> friendsIds = userStorage.findById(userId).getFriends();
        Set<Long> friendsOfFriend = userStorage.findById(friendId).getFriends();
        friendsIds.remove(friendId);
        friendsOfFriend.remove(userId);
    }

    public Set<Long> getCommonFriends(Long userId, Long friendId) {
        Set<Long> friendsIds = userStorage.findById(userId).getFriends();
        Set<Long> friendsOfFriend = userStorage.findById(friendId).getFriends();
        Set<Long> commonFriends = new HashSet<>();

        if (friendsIds == null) {
            return new HashSet<>();
        }

        for (Long fr : friendsIds) {
            if (friendsOfFriend.contains(fr)) {
                commonFriends.add(fr);
            }
        }

        return commonFriends;
    }
}
