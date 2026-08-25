package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@Accessors(chain = true)
public class Film {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MpaRating mpa;
    private Set<Genre> genres = new TreeSet<>();
    private int countLikes = 0;
    private Set<Director> directors = new TreeSet<>();
}
