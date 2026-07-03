package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(Integer id) {
        return userStorage.findById(id);
    }

    public User save(User user) {
        return userStorage.save(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public User addFriend(Integer id, Integer friendId) {
        return userStorage.addFriend(id, friendId);
    }

    public User removeFriend(Integer id, Integer friendId) {
        return userStorage.removeFriend(id, friendId);
    }

    public Set<User> findAllFriends(Integer id) {
        return userStorage.findAllFriends(id);
    }

    public Set<User> findCommonFriends(Integer id, Integer otherId) {
        return userStorage.findCommonFriends(id, otherId);
    }
}
