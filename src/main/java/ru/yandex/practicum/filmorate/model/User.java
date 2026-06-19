package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Integer id; //целочисленный идентификатор — id;
    @NotBlank
    @Email
    private String email; //электронная почта — email;
    @NotBlank
    private String login; //логин пользователя — login;
    private String name; //имя для отображения — name;
    @Past
    private LocalDate birthday; //дата рождения — birthday.
}
