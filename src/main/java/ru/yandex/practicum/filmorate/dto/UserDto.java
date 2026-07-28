package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class UserDto {

    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
