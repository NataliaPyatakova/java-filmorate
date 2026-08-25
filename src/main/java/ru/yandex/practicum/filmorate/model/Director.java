package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Director implements Comparable<Director> {

    private Integer id;
    private String name;

    @Override
    public int compareTo(Director o) {
        return this.id.compareTo(o.id);
    }
}
