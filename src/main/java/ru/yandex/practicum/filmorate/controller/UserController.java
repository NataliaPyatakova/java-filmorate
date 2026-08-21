package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final EventService eventService;

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") @NotNull Integer id) {
        return userService.findById(id);
    }

    @PostMapping
    public UserDto save(@Validated @RequestBody NewUserDto user) {
        return userService.save(user);
    }

    @PutMapping
    public UserDto update(@Validated @RequestBody UpdateUserDto updateUserDto) {
        return userService.update(updateUserDto);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public UserDto addFriend(@PathVariable("id") @NotNull Integer id, @PathVariable("friendId") @NotNull Integer friendId) {
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public UserDto removeFriend(@PathVariable("id") @NotNull Integer id, @PathVariable("friendId") @NotNull Integer friendId) {
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<UserDto> findAllFriends(@PathVariable("id") @NotNull Integer id) {
        return userService.findAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<UserDto> findCommonFriends(@PathVariable("id") @NotNull Integer id, @PathVariable("otherId") @NotNull Integer otherId) {
        return userService.findCommonFriends(id, otherId);
    }

    @GetMapping("/{id}/recommendations")
    public List<FilmDto> getRecommendations(@PathVariable Integer id) {
        return userService.getRecommendations(id);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
    }

    @GetMapping("/{id}/feed")
    public List<EventDto> getFeed(@PathVariable Integer id) {
        return eventService.getFeed(id);
    }
}
