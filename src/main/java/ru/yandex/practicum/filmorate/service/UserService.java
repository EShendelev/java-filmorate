package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.findById(userId);
        User friend = userStorage.findById(friendId);

        boolean addition = friendStorage.addFriend(userId, friendId);

    }

    public void deleteFriend(Long userId, Long friendId) {
        List<Long> friendsIds = userStorage.findById(userId).getFriends();
        List<Long> friendsOfFriend = userStorage.findById(friendId).getFriends();
        friendsIds.remove(friendId);
        friendsOfFriend.remove(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<Long> friendsIds = userStorage.findById(userId).getFriends();
        List<Long> friendsOfFriend = userStorage.findById(friendId).getFriends();
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

//    public Collection<User> getFriendsList(Collection<Long> idSet) {
//        return userStorage.getFriendList(idSet);
//    }
}
