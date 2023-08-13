package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        userStorage.findById(userId);
        userStorage.findById(friendId);
        boolean removal = friendStorage.removeFromFriends(userId, friendId);

        if (!removal) {
            throw new ObjectNotFoundException(String.format("Пользователь с id %d не в списке друзей" +
                    " у пользователя с id %s", friendId, userId));
        }
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
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> getListOfFriends(long id) {
        userStorage.findById(id);
        return friendStorage.getListOfFriends(id).stream().map(userStorage::findById).collect(Collectors.toList());
    }

    public List<User> getListOfAMutualFriends(long id, long otherId) {
        userStorage.findById(id);
        userStorage.findById(otherId);
        return friendStorage.getListOfMutualFriends(id, otherId).stream()
                .map(userStorage::findById).collect(Collectors.toList());
    }

}
