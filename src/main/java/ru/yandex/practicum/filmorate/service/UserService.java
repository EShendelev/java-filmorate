package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.EventStorage;
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
    private final EventStorage eventStorage;

    public void addFriend(Long userId, Long friendId) {
        boolean checkUser = userStorage.checkById(userId);
        boolean checkFriend = userStorage.checkById(friendId);
        if (checkFriend && checkUser) {
            boolean addition = friendStorage.addFriend(userId, friendId);
            eventStorage.add(userId, friendId, EventTypes.FRIEND, EventOperations.ADD);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        boolean checkUser = userStorage.checkById(userId);
        boolean checkFriend = userStorage.checkById(friendId);
        if (checkFriend && checkUser) {
            boolean removal = friendStorage.removeFromFriends(userId, friendId);
            if (!removal) {
                throw new ObjectNotFoundException(String.format("Пользователь с id %d не в списке друзей" +
                        " у пользователя с id %s", friendId, userId));
            }
            eventStorage.add(userId, friendId, EventTypes.FRIEND, EventOperations.REMOVE);
        }
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<Long> commonFriendsIds = new ArrayList<>();
        if (userStorage.checkById(userId) && userStorage.checkById(friendId)) {
            commonFriendsIds = friendStorage.getListOfMutualFriends(userId, friendId);
        }
        return userStorage.getUsersByListIds(commonFriendsIds);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public boolean checkById(long id) {
        return userStorage.checkById(id);
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

    public List<User> getListOfFriends(long userId) {
        List<User> friends = new ArrayList<>();
        if (checkById(userId)) {
            friends = userStorage.getFriendsByUserId(userId);
        }
        return friends;
    }

    public void deleteUserById(long id) {
        userStorage.deleteUserById(id);
    }

    public Collection<Event> getUserEvents(long id) {
        Collection<Event> events = new ArrayList<>();
        if (checkById(id)) {
            events = eventStorage.getUserEvents(id);
        }
        return events;
    }

    public Long getSimilarId(long id) {
        return userStorage.getSimilarId(id);
    }
}
