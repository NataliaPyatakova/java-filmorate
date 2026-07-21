package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import ru.yandex.practicum.filmorate.validation.OnCreate;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@EqualsAndHashCode(exclude = {"likes"})
@Accessors(chain = true)  //TODO разобраться с конфликтом fluent = true и @Validated
public class Film {

    @NotNull(groups = OnUpdate.class) //проверяем на наличие только при обновлении
    private Integer id; //целочисленный идентификатор — id;
    @NotBlank(groups = OnCreate.class) //проверяем на наличие только при создании
    private String name; //название — name;
    @Size(max = 200) //проверяем всегда на корректность
    private String description; //описание — description;
    @NotNull(groups = OnCreate.class) //проверяем на наличие только при создании
    private LocalDate releaseDate;//дата релиза — releaseDate;
    @NotNull(groups = OnCreate.class) //проверяем на наличие только при создании
    @Positive //проверяем всегда на корректность
    private int duration; //продолжительность фильма — duration.
    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();
    private MpaRating mpa;
    private Set<Genre> genres = new TreeSet<>();

    public Integer countLikes() {
        return likes.size();
    }
}
