package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;

@Data
public class User {

    @NotNull(groups = OnUpdate.class)  //проверяем на наличие только при обновлении
    private Integer id; //целочисленный идентификатор — id;
    @NotBlank(groups = OnCreate.class) //проверяем на наличие только при создании
    @Email  //проверяем всегда на корректность
    private String email; //электронная почта — email;
    @NotBlank(groups = OnCreate.class) //проверяем на наличие только при создании
    private String login; //логин пользователя — login;
    private String name; //имя для отображения — name;
    @NotNull(groups = OnCreate.class) //проверяем на наличие только при создании
    @PastOrPresent  //проверяем всегда на корректность
    private LocalDate birthday; //дата рождения — birthday.
}
