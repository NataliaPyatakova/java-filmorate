package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@Accessors(chain = true)
public class FilmDto {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private MpaRatingDto mpa;
    private Set<GenreDto> genres = new TreeSet<>();
    private Set<DirectorDto> directors = new TreeSet<>();
}
