package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;

@Data
public class Film {

    @NotNull(groups = OnUpdate.class) //проверяем на наличие только при обновлении
    private Integer id; //целочисленный идентификатор — id;
    @NotBlank(groups = OnCreate.class) //проверяем на наличие только при создании
    private String name; //название — name;
    @Size(max = 200) //проверяем всегда на корректность
    private String description; //описание — description;
    @NotNull(groups = OnCreate.class) //проверяем на наличие только при создании
    private LocalDate releaseDate;//дата релиза — releaseDate;
    @Positive //проверяем всегда на корректность
    private int duration; //продолжительность фильма — duration.
}
