package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GenreDto implements Comparable<GenreDto> {

    @NotNull
    private Integer id;
    @NotBlank
    private String name;

    @Override
    public int compareTo(GenreDto o) {
        return this.id.compareTo(o.id);
    }
}
