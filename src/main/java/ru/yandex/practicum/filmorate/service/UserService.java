package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
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
        return userStorage.findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public User save(User user) {
        log.info("Saving user {}", user);
        replaceName(user);
        log.debug("Replaced userName for save {}", user.getName());
        validateEmail(user.getEmail());
        return userStorage.save(user);
    }

    public User update(User newUser) {
        log.info("Updating newUser {}", newUser);
        User oldUser = findById(newUser.getId());
        log.debug("Old user for update {}", oldUser);
        replaceName(newUser);
        log.debug("Replaced userName for update {}", newUser.getName());
        validateEmail(newUser.getEmail());
        boolean updateName = false;  //TODO поискать решение получше
        boolean updateEmail = false;
        boolean updateLogin = false;
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) { //пустоту разрешаем, но не записываем
            updateEmail = true;
        }
        if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) { //пустоту разрешаем, но не записываем
            updateLogin = true;
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()) { //пустоту разрешаем, но не записываем
            updateName = true;
        }
        return userStorage.update(newUser, oldUser, updateName, updateEmail, updateLogin);
    }

    public User addFriend(Integer id, Integer friendId) {
        log.info("Adding friend {} to User {}", friendId, id);
        User user = findById(id);
        User friend = findById(friendId);
        if (Objects.equals(id, friendId)) {
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }
        //добавляем пользователя другу
        userStorage.addFriend(user, friend);
        //добавляем другу пользователя //условие тестов
        userStorage.addFriend(friend, user);
        return user;
    }

    public User removeFriend(Integer id, Integer friendId) {
        log.info("Removing friend {} from User {}", friendId, id);
        User user = findById(id);
        User friend = findById(friendId);
        //удаляем друга у пользователя
        userStorage.removeFriend(user, friend);
        //удаляем пользователя у друга //условие тестов
        userStorage.removeFriend(friend, user);
        return user;
    }

    public List<User> findAllFriends(Integer id) {
        log.info("Finding all friends from User {}", id);
        User user = findById(id);
        return userStorage.findAllFriends(user);
    }

    public Set<User> findCommonFriends(Integer id, Integer otherId) {
        log.info("Finding common friends from User {} to User {}", id, otherId);
        User user = findById(id);
        User otherUser = findById(otherId);
        return userStorage.findCommonFriends(user, otherUser);
    }

    public void deleteAll() {
        userStorage.deleteAll();
    }

    private static void replaceName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    private void validateEmail(String email) {
        if (email != null && !email.isBlank() && userStorage.existEmail(email)) {
            throw new ValidationException("Пользователь с такой почтой уже существует!");
        }
    }
}
