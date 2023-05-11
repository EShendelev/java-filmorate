package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
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
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        if (user == null) {
            throw new UserNotExistException(String.format("пользователя с id %d не существует", userId));
        }
        if (friend == null) {
            throw new UserNotExistException(String.format("пользователя с id %d не существует", friendId));
        }

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
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

        for (Long fr : friendsIds) {
            if (friendsOfFriend.contains(fr)) {
                commonFriends.add(userStorage.findById(fr));
            }
        }

        return commonFriends;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Long id) {
        return userStorage.findById(id);
    }

    public User add(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> getFriendsList(Collection<Long> idSet) {
        return userStorage.getFriendList(idSet);
    }
}
