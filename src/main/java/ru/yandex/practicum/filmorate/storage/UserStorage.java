package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    List<User> findAll();

    User save(User user);

    User update(User user);

    Optional<User> findById(Integer id);

    void addFriend(User user, User friend);

    void removeFriend(User user, User friend);

    List<User> findAllFriends(User user);

    Set<User> findCommonFriends(User user, User otherUser);

    void deleteAll();

    boolean existEmail(String email);
}

