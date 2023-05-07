package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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

    public List<User> getCommonFriends(Long userId, Long friendId) {
        Set<Long> friendsIds = userStorage.findById(userId).getFriends();
        Set<Long> friendsOfFriend = userStorage.findById(friendId).getFriends();
        List<User> commonFriends = new ArrayList<>();
        if (friendsIds == null) {
            return new ArrayList<>();
        }

        for (Long fr : friendsIds) {
            if (friendsOfFriend.contains(fr)) {
                commonFriends.add(userStorage.findById(fr));
            }
        }

        return commonFriends;
    }
}
