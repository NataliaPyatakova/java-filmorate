package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Genre implements Comparable<Genre> {

    private Integer id;
    private String name;

    @Override
    public int compareTo(Genre o) {
        return this.id.compareTo(o.id);
    }
}
