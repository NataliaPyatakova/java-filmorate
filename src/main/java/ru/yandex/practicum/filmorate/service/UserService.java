package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.NewUserDto;
import ru.yandex.practicum.filmorate.dto.UpdateUserDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<UserDto> findAll() {
        return userStorage.findAll().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public UserDto findById(Integer id) {
        return UserMapper.mapToUserDto(findUserById(id));
    }

    public UserDto save(NewUserDto newUserDto) {
        log.info("Saving user {}", newUserDto);
        User user = UserMapper.mapToUser(newUserDto);
        replaceName(user);
        log.debug("Replaced userName for save {}", user.getName());
        validateEmail(user.getEmail());
        User newUser = userStorage.save(user);
        return UserMapper.mapToUserDto(newUser);
    }

    public UserDto update(UpdateUserDto updateUserDto) {
        log.info("Updating newUser {}", updateUserDto);
        User oldUser = findUserById(updateUserDto.getId());
        log.debug("Old user for update {}", oldUser);
        validateEmail(updateUserDto.getEmail());
        //сливаем воедино пришедшие данные и данные в базе-формируем целиком юзера
        User newUser = UserMapper.updateUserFields(oldUser, updateUserDto);
        User updatedUser = userStorage.update(newUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public UserDto addFriend(Integer id, Integer friendId) {
        log.info("Adding friend {} to User {}", friendId, id);
        User user = findUserById(id);
        User friend = findUserById(friendId);
        if (Objects.equals(id, friendId)) {
            throw new ValidationException("Нельзя добавить в друзья самого себя");
        }
        //добавляем пользователя другу
        userStorage.addFriend(user, friend);
        return UserMapper.mapToUserDto(user);
    }

    public UserDto removeFriend(Integer id, Integer friendId) {
        log.info("Removing friend {} from User {}", friendId, id);
        User user = findUserById(id);
        User friend = findUserById(friendId);
        //удаляем друга у пользователя
        userStorage.removeFriend(user, friend);
        return UserMapper.mapToUserDto(user);
    }

    public List<UserDto> findAllFriends(Integer id) {
        log.info("Finding all friends from User {}", id);
        User user = findUserById(id);
        return userStorage.findAllFriends(user).stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public Set<UserDto> findCommonFriends(Integer id, Integer otherId) {
        log.info("Finding common friends from User {} to User {}", id, otherId);
        User user = findUserById(id);
        User otherUser = findUserById(otherId);
        return userStorage.findCommonFriends(user, otherUser).stream().map(UserMapper::mapToUserDto).collect(Collectors.toSet());
    }

    public void deleteAll() {
        userStorage.deleteAll();
    }

    private User findUserById(Integer id) {
        return userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
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
