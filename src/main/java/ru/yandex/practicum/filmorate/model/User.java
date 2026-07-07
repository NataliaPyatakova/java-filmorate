package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"birthday", "friends"})
@Accessors(chain = true) //TODO разобраться с конфликтом fluent = true и @Validated
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
    @JsonIgnore
    private Set<Integer> friends = new HashSet<>();
}