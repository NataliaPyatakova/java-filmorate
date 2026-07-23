package ru.yandex.practicum.filmorate.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.NewUserDto;
import ru.yandex.practicum.filmorate.dto.UpdateUserDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setLogin(user.getLogin());
        dto.setName(user.getName());
        dto.setBirthday(user.getBirthday());
        return dto;
    }

    public static User mapToUser(NewUserDto newUserDto) {
        User user = new User();
        user.setEmail(newUserDto.getEmail());
        user.setLogin(newUserDto.getLogin());
        user.setName(newUserDto.getName());
        user.setBirthday(newUserDto.getBirthday());
        return user;
    }

    public static User updateUserFields(User oldUser, UpdateUserDto updateUserDto) {
        User newUser = new User();
        if (updateUserDto.hasLogin()) {
            newUser.setLogin(updateUserDto.getLogin());
        } else {
            newUser.setLogin(oldUser.getLogin());
        }
        if (updateUserDto.hasEmail()) {
            newUser.setEmail(updateUserDto.getEmail());
        } else {
            newUser.setEmail(oldUser.getEmail());
        }
        if (updateUserDto.hasName()) {
            newUser.setName(updateUserDto.getName());
        } else {
            newUser.setName(oldUser.getName());
        }
        if (updateUserDto.hasBirthday()) {
            newUser.setBirthday(updateUserDto.getBirthday());
        } else {
            newUser.setBirthday(oldUser.getBirthday());
        }
        return newUser;
    }
}
