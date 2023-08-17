package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotExistException;
import ru.yandex.practicum.filmorate.exception.UserNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.storage.utils.UserIdProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    UserIdProvider idProvider;
    private final Map<Long, User> users = new HashMap<>();

    public InMemoryUserStorage(UserIdProvider userIdProvider) {
        this.idProvider = userIdProvider;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User add(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Long id = idProvider.getIncrementId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new FilmNotExistException(String.format("Ошибка обновления данных. " +
                    "Пользователь с  id %d не найден", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }
    
    //заглушка для работы с внешней БД
    @Override
    public List<User> getUsersByListIds(List<Long> ids) {
        return null;
    }

    //заглушка для работы с внешней БД
    @Override
    public boolean checkById(long id) {
        return false;
    }

    @Override
    public User findById(Long id) throws UserNotExistException {
        if (!users.containsKey(id)) {
            throw new UserNotExistException(String.format("Ошибка поиска. Пользователь id %d не найден", id));
        }
        return users.get(id);
    }

    public Collection<User> getFriendList(Collection<Long> idSet) {
        if (idSet.isEmpty()) {
            throw new UserNotExistException("Ошибка вывода списка друзей. Список друзей пуст");
        }
        return idSet.stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }
}
