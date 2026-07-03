package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List<User> findAll();

    User save(User user);

    User update(User newUser);

    User findById(Integer id);

    User addFriend(Integer id, Integer friendId);

    User removeFriend(Integer id, Integer friendId);

    Set<User> findAllFriends(Integer id);

    Set<User> findCommonFriends(Integer id, Integer otherId);

}

