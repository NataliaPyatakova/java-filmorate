package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {

    private Integer id; //целочисленный идентификатор — id;
    @NotBlank
    private String name; //название — name;
    @Size(max = 200)
    private String description; //описание — description;
    private LocalDate releaseDate;//дата релиза — releaseDate;
    @Positive
    private int duration; //продолжительность фильма — duration.
}
