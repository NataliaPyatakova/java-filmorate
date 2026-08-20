package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MpaRating implements Comparable<MpaRating> {

    @NotNull
    private Integer id;
    @NotBlank
    private String name;

    @Override
    public int compareTo(MpaRating o) {
        return this.id.compareTo(o.id);
    }
}
