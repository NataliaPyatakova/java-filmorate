package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(exclude = {"birthday"})
@Accessors(chain = true)
public class User {

    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}