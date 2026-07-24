package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();

    @Override
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @Override
    public User save(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        return user;
    }

    @Override
    public Optional<User> findById(Integer id) {
        return users.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriends().add(friend.getId());
    }

    @Override
    public void removeFriend(User user, User friend) {
        user.getFriends().remove(friend.getId());
    }

    @Override
    public List<User> findAllFriends(User user) {
        return users.values().stream()
                .filter(friend -> user.getFriends().contains(friend.getId())).toList();
    }

    @Override
    public Set<User> findCommonFriends(User user, User otherUser) {
        return user.getFriends().stream()
                .filter(friend -> otherUser.getFriends().contains(friend))
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteAll() {
        users.clear();
        emails.clear();
    }

    @Override
    public boolean existEmail(String email) {
        return emails.contains(email);
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
