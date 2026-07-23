package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Data
@EqualsAndHashCode(exclude = {"likes"})
@Accessors(chain = true)
public class Film {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Integer> likes = new HashSet<>();
    private MpaRating mpa;
    private Set<Genre> genres = new TreeSet<>();

    public Integer countLikes() {
        return likes.size();
    }
}
