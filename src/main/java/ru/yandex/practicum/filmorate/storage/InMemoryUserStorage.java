package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public User save(User user) {
        log.info("Saving user {}", user);
        replaceName(user);
        log.debug("Replaced userName for save {}", user.getName());
        user.setId(getNextId());
        log.debug("Generated userId {}", user.getId());
        if (user.getFriends() == null) {  //возможно сделать лучше - поискать
            Set<Integer> friends =  new HashSet<>();
            user.setFriends(friends);
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        log.info("Updating newUser {}", newUser);
        findById(newUser.getId());
        replaceName(newUser);
        log.debug("Replaced userName for update {}", newUser.getName());
        User oldUser = users.get(newUser.getId());
        log.debug("Old user for update {}", oldUser);
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) { //пустоту разрешаем, но не записываем
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getLogin() != null && !newUser.getLogin().isBlank()) { //пустоту разрешаем, но не записываем
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getName() != null && !newUser.getName().isBlank()) { //пустоту разрешаем, но не записываем
            oldUser.setName(newUser.getName());
        }
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    @Override
    public User findById(Integer id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
    }

    @Override
    public User addFriend(Integer id, Integer friendId) {
        log.info("Adding friend {} to User {}", friendId, id);
        User user = findById(id);
        User friend = findById(friendId);
        if (Objects.equals(id, friendId)) {
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        return user;
    }

    @Override
    public User removeFriend(Integer id, Integer friendId) {
        log.info("Removing friend {} from User {}", friendId, id);
        User user = findById(id);
        User friend = findById(friendId);
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        return user;
    }

    @Override
    public List<User> findAllFriends(Integer id) {
        log.info("Finding all friends from User {}", id);
        User user = findById(id);
        return users.values().stream()
                .filter(friend -> user.getFriends().contains(friend.getId())).toList();
    }

    @Override
    public Set<User> findCommonFriends(Integer id, Integer otherId) {
        log.info("Finding common friends from User {} to User {}", id, otherId);
        User user = findById(id);
        User otherUser = findById(otherId);
        return user.getFriends().stream()
                .filter(friend -> otherUser.getFriends().contains(friend))
                .map(this::findById)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    private static void replaceName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    private Integer getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
