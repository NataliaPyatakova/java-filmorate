package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public List<User> findAll() {
        return users.values().stream().toList();
    }

    @PostMapping
    public User save(@Validated({OnCreate.class, Default.class}) @RequestBody User user) {
        log.info("Saving user {}", user);
        replaceName(user);
        log.debug("Replaced userName for save {}", user.getName());
        user.setId(getNextId());
        log.debug("Generated userId {}", user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@Validated({OnUpdate.class, Default.class}) @RequestBody User newUser) {
        log.info("Updating newUser {}", newUser);
        if (users.containsKey(newUser.getId())) {
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
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
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

