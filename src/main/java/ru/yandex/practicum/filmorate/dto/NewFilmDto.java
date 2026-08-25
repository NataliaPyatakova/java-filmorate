package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@Accessors(chain = true)
public class NewFilmDto {

    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @NotNull
    @Positive
    private int duration;
    private MpaRatingDto mpa;
    private Set<GenreDto> genres = new TreeSet<>();
    private Set<DirectorDto> directors = new TreeSet<>();
}
